package com.lsl.smartweb.view;

import com.lsl.smartweb.utils.StringUtils;
import com.lsl.smartweb.utils.Util;
import net.coobird.thumbnailator.Thumbnails;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by LSL on 2018\6\22 0022
 * 描述：
 * 版本：1.0.0
 */
public class ReturnUtls {
    private static final String REDIRECT = "redirect:";
    private static final String FORWARD = "forward:";
    private static final String FILE = "file:";
    private static final String IMG = "img:";
    private static final Map<String, String> contenMap = new ConcurrentHashMap<>();

    public static void dual(Object o, HttpServletResponse response, HttpServletRequest request, String charset) {
        response.setCharacterEncoding(charset);
        try {
            if (o instanceof String) {
                String s = (String) o;
                if (s.startsWith(IMG)) {
                    String url = s.replaceFirst(IMG, "");
                    String[] split = url.split(",size=");
                    if (split.length == 2) {
                        String[] h = split[1].split(",filename=");
                        if (h.length == 2) {
                            imgDual(response, split[0], h[1], h[0]);
                        } else {
                            imgDual(response, split[0], "", h[0]);
                        }
                    } else {
                        imgDual(response, split[0], "", "");
                    }
                } else if (s.startsWith(FILE)) {
                    String url = s.replaceFirst(FILE, "");
                    String[] split = url.split(",filename=");
                    if (split.length == 1) {
                        fileDual(response, split[0], null);
                    } else {
                        fileDual(response, split[0], split[1]);
                    }
                } else if (s.startsWith(REDIRECT)) {
                    String url = s.replaceFirst(REDIRECT, "");
                    redictDual(response, url);
                } else if (s.startsWith(FORWARD)) {
                    String url = s.replaceFirst(FORWARD, "");
                    forwardDual(request, response, url);
                } else {
                    textDual(response, s);
                }
            } else if (o instanceof SmartInputStrem) {
                SmartInputStrem sim = (SmartInputStrem) o;
                inputStremDual(response, sim.getInputStream(), sim.getFilename());
            } else {
                jsonDual(response, o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法名: ReturnUtls.imgDual
     * 作者: LSL
     * 创建时间: 13:15 2018\7\3 0003
     * 描述: tupian
     * 参数: [response, filepath, filename, size]
     * 返回: void
     */
    private static void imgDual(HttpServletResponse response, String filepath, String filename, String size) throws IOException {
        File file = new File(filepath);
        String[]  xes = size.split("x");
        ServletOutputStream outputStream = response.getOutputStream();
        if (StringUtils.isNotEmpty(filename)) {
            String s = filename.substring(filename.lastIndexOf("."));
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType(contenMap.containsKey(s) ? contenMap.get(s) : "application/octet-stream");
        }
        if(!StringUtils.isNotEmpty(size)){
            Thumbnails.of(filepath).scale(1).toOutputStream(outputStream);
        }else if (xes.length == 2) {
            if (file.exists()) {
                ImgUtils.imgSizetoOut(filepath,size,outputStream);

            } else {
                textDual(response, "文件丢失:" + filepath);
            }
        } else {
            textDual(response, "尺寸设置有误（正确的例子：200(宽)x300(高)）:" + size);
        }
    }

    /**
     * 方法名: ReturnUtls.fileDual
     * 作者: LSL
     * 创建时间: 12:41 2018\6\29 0029
     * 描述: 处理file：
     * 参数: []
     * 返回: void
     */
    private static void fileDual(HttpServletResponse response, String filepath, String filename) throws IOException {
        File file = new File(filepath);
        if (filename == null) {
            filename = file.getName();
        }
        if (file.exists()) {
            InputStream inputStream = new FileInputStream(file);
            inputStremDual(response, inputStream, filename);
        } else {
            textDual(response, "文件丢失:" + filepath);
        }
    }

    /**
     * 方法名: ReturnUtls.inputStremDual
     * 作者: LSL
     * 创建时间: 17:33 2018\6\29 0029
     * 描述: 数据输入流处理
     * 参数: [response, in, isView, filename]
     * 返回: void
     */
    private static void inputStremDual(HttpServletResponse response, InputStream inputStream, String filename) throws IOException {
        String s = filename.substring(filename.lastIndexOf("."));
        OutputStream out = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType(contenMap.containsKey(s) ? contenMap.get(s) : "application/octet-stream");
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        inputStream.close();
        out.flush();
        out.close();
    }

    /**
     * 方法名: ReturnUtls.redictDual
     * 作者: LSL
     * 创建时间: 12:48 2018\6\29 0029
     * 描述: 重定向
     * 参数: [response, url]
     * 返回: void
     */
    private static void redictDual(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    /**
     * 方法名: ReturnUtls.forwardDual
     * 作者: LSL
     * 创建时间: 12:48 2018\6\29 0029
     * 描述: 转发请求
     * 参数: [request, url]
     * 返回: void
     */
    private static void forwardDual(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * 方法名: ReturnUtls.jsonDual
     * 作者: LSL
     * 创建时间: 12:48 2018\6\29 0029
     * 描述: 返回值为json
     * 参数: [response, obj]
     * 返回: void
     */
    private static void jsonDual(HttpServletResponse response, Object obj) throws IOException {
        //处理返回值
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        String s = Util.toJson(obj);
        writer.write(s);
        writer.flush();
        writer.close();
    }

    /**
     * 方法名: ReturnUtls.jsonDual
     * 作者: LSL
     * 创建时间: 12:48 2018\6\29 0029
     * 描述: 返回值为json
     * 参数: [response, obj]
     * 返回: void
     */
    private static void textDual(HttpServletResponse response, String s) throws IOException {
        //处理返回值
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.write(s);
        writer.flush();
        writer.close();
    }

    public static void init(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                String[] ys = line.split("=");
                if (ys != null && ys.length == 2) {
                    contenMap.put(ys[0], ys[1]);
                }
            }
            System.out.println("FileContentType__init__success：" + contenMap.size());
        } catch (IOException e) {
            System.out.println("FileContentType__init__false");
            throw new RuntimeException(e);
        }
    }

}
