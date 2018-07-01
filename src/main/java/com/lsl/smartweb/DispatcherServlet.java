package com.lsl.smartweb;

import com.lsl.smartweb.aop.ExceptionHandler;
import com.lsl.smartweb.aop.core.ControllerHelper;
import com.lsl.smartweb.aop.core.Handler;
import com.lsl.smartweb.aop.core.Request;
import com.lsl.smartweb.configure.ReadXml;
import com.lsl.smartweb.configure.SmartConfig;
import com.lsl.smartweb.core.*;
import com.lsl.smartweb.fileup.SmartFile;
import com.lsl.smartweb.fileup.UpStatus;
import com.lsl.smartweb.fileup.UploadProcessListener;
import com.lsl.smartweb.utils.ArrayUtils;
import com.lsl.smartweb.utils.StringUtils;
import com.lsl.smartweb.utils.Util;
import com.lsl.smartweb.view.ReturnUtls;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：请求分发器
 * 版本：1.0.0
 */
@WebServlet(urlPatterns = "/", loadOnStartup = 10)
public class DispatcherServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private final static String charset="utf-8";
    private ExceptionHandler exceptionHandler;
    private DiskFileItemFactory fac;

    /**
     * 方法名: DispatcherServlet.init
     * 作者: LSL
     * 创建时间: 15:58 2018\5\9 0009
     * 描述: 初始化
     * 参数: [config]
     * 返回: void
     */
    @Override
    public void init(ServletConfig config) {
        log.debug("smartmvc 获取上下文！");
        //获取上下文
        ServletContext servletContext = config.getServletContext();
        log.info("配置初始化...");
        ReadXml.read(this.getClass().getClassLoader().getResource("").getPath());
        //初始化自定义helper相关类
        log.info("DispatcherServlet 开始初始化！");
        HelperLoader.init();
        //注册处理JSP的servlet
        log.info("DispatcherServlet 注册jsp！");
        ServletRegistration jsp = servletContext.getServletRegistration("jsp");
        jsp.addMapping("*.jsp", "*.jspx");
        //注册静态资源处理的默认servlet
        log.info("DispatcherServlet 注册静态资源！");
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("*.html", "*.js", "*.css", "*.jpg", "*.swf", "*.png", "*.gif", "*.svg", "*.ppt", "*.pptx", "*.doc", "*.docx", "*.xls", "*.xlsx", "*.ico");
        if (ArrayUtils.isNotEmpty(SmartConfig.getResource())) {
            log.debug("初始化自添加静态资源：{}", Arrays.toString(SmartConfig.getResource()));
            defaultServlet.addMapping(SmartConfig.getResource());
        }
        //文件上传工厂处理
        fac = new DiskFileItemFactory();//1.创建文件上传工厂类
        fac.setSizeThreshold(SmartConfig.getUpload_usecachesize());//超过多少大小使用磁盘缓存
        if(StringUtils.isNotEmpty(SmartConfig.getUpload_tempdir())){
            fac.setRepository(new File(SmartConfig.getUpload_tempdir()));
        }

        log.debug("文件上传工厂初始化完成");
        //文件上传进度servlet初始化
        ControllerHelper.changeHandler(new Request("get","/fileupload_process"),new Request("get",SmartConfig.getProgress()) );
        log.debug("进度信息Controller初始化完成");
        if (StringUtils.isNotEmpty(SmartConfig.getException())) {
            log.debug("初始化异常处理类：{}", SmartConfig.getException());
            try {
                exceptionHandler = (ExceptionHandler) Class.forName(SmartConfig.getException()).newInstance();
            } catch (Exception e) {
                log.error("统一异常处理类初始化失败：{}", SmartConfig.getException(), e);
            }
        }
        log.info("smartmvc 初始化完成！");
    }

    /**
     * 方法名: DispatcherServlet.service
     * 作者: LSL
     * 创建时间: 15:58 2018\5\11 0011
     * 描述: 分发器
     * 参数: [req, res]
     * 返回: void
     */
    @Override
    public void service(ServletRequest req, ServletResponse res){
        Object o = null;
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getServletPath();
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            log.debug("来访路径：{},绑定方法：{}.{}()", requestPath, handler.getContriller().getName(), handler.getMethod().getName());
            //存放常用域
            HashMap<String, Object> val = new HashMap<>();
            val.put("request", request);
            val.put("response", response);
            val.put("req", req);
            val.put("res", res);
            val.put("session", request.getSession());
            val.put("application", request.getServletContext());

            HashMap<String, Object> parmap = null;
            Param param;
            try {
                if(ServletFileUpload.isMultipartContent(request)){//文件表单
                    parmap = dualFile(request);
                    param = new Param(parmap);
                }else{//参数处理
                    parmap = parmDual(request);
                    param = new Param(parmap);
                }
                Method method = handler.getMethod();//获得调用方法
                Class<?> controller = handler.getContriller();//获得对象
                Object bean = BeanHelper.getBean(controller);//获得对像实例
                o = BeanFactory.invokeMethod(bean, method, val, param);//调用业务
            } catch (Exception e) {
                if (exceptionHandler == null) {
                    o = e;
                } else {
                    if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
                            .getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
                        //不是ajax
                        o = exceptionHandler.resolveException_View(request, response, parmap, e);
                    } else {
                        //ajax
                        o = exceptionHandler.resolveException_Json(request, response, parmap, e);
                    }
                }
            }
        } else {
            o = SmartConfig.getInterfaceNotFond().replace("${requestUrl}", request.getRequestURI());
        }
        //返回值处理
        returnDual(o, response, request);
    }

    /**
     * 方法名: DispatcherServlet.parmDual
     * 作者: LSL
     * 创建时间: 15:58 2018\5\11 0011
     * 描述: 参数处理
     * 参数: [handler, request]
     * 返回: java.util.HashMap<java.lang.String,java.lang.Object>
     */
    private HashMap<String, Object> parmDual(HttpServletRequest request) throws IOException {
        HashMap<String, Object> parmap = new HashMap<String, Object>();
       /* //装载get参数
        String body = Util.decodeURL(Util.getString(request.getInputStream()));
        if (StringUtils.isNotEmpty(body)) {
            String[] params = body.split("&");
            if (params != null && params.length > 0) {
                for (String param : params) {
                    String[] arr = param.split("=");
                    if (arr != null && arr.length == 2) {
                        parmap.put(arr[0], arr[1]);
                    }else if(arr != null && arr.length == 1){
                        parmap.put(arr[0], "");
                    }
                }
            }
        }*/
        Enumeration<String> parameterNames = request.getParameterNames();
//        Map<String, String[]> parameterMap = request.getParameterMap();
        while (parameterNames.hasMoreElements()) {
            String s = parameterNames.nextElement();
            String parameter = request.getParameter(s);
            parmap.put(s, parameter);
        }
        return parmap;
    }

    /**
     * 方法名: DispatcherServlet.returnDual
     * 作者: LSL
     * 创建时间: 15:59 2018\5\11 0011
     * 描述: 返回值处理
     * 参数: [o, response, request]
     * 返回: void
     */
    private void returnDual(Object o, HttpServletResponse response, HttpServletRequest request){
        if (null != o) {
            ReturnUtls.dual(o, response, request,charset);
        }
    }
    /**
     * 方法名: DispatcherServlet.dualFile
     * 作者: LSL
     * 创建时间: 10:59 2018\6\26 0026
     * 描述: 文件上传表单处理
     * 参数: [request]
     * 返回: void
     */
    private HashMap<String, Object> dualFile(HttpServletRequest request) throws IOException, FileUploadException {
        HashMap<String, Object> parmap = new HashMap<String, Object>();
        UpStatus upStatus = new UpStatus();
        ServletFileUpload upload = new ServletFileUpload(fac);//2.创建文件上传核心类对象
        upload.setFileSizeMax(SmartConfig.getUpload_filemaxsize());//单个文件大小
        upload.setSizeMax(SmartConfig.getUpload_maxsize()); //总文件大小
        upload.setHeaderEncoding(charset);
        upload.setProgressListener(new UploadProcessListener(upStatus,request.getSession().getId()));
        try {
            List<FileItem>  items = upload.parseRequest(request);// 1. 得到 FileItem 的集合 items
            upStatus.setFilenums(items.size());
            for (FileItem item : items) {// 2. 遍历 items:
                if (item.isFormField()) {// 若是一个一般的表单域, 打印信息
                    String name = item.getFieldName();
                    String value = item.getString(charset);
                    parmap.put(name,value);
                }else {// 若是文件域则把文件保存到 e:\\files 目录下.
                    String fileName = item.getFieldName();
                    String contentType = item.getContentType();
                    InputStream in = item.getInputStream();
                    long size = item.getSize();
                    File tmpfile = ((DiskFileItem) item).getStoreLocation();
                    List<SmartFile> ls = (List<SmartFile>) parmap.get(fileName);
                    if(ls ==null){
                        ls = new ArrayList<>();
                    }
                    ls.add(new SmartFile(in,item.getName(),fileName,contentType,size,tmpfile));
                    parmap.put(fileName,ls);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("处理文件表单出现错误：",e);
            throw e;
        }
        return parmap;
    }
}
