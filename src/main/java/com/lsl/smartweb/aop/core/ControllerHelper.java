package com.lsl.smartweb.aop.core;


import com.lsl.smartweb.annotion.Controller;
import com.lsl.smartweb.annotion.GET;
import com.lsl.smartweb.annotion.POST;
import com.lsl.smartweb.core.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：controller处理类
 * 版本：1.0.0
 */
public final class ControllerHelper {
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();
    private static final Logger log = LoggerFactory.getLogger(ControllerHelper.class);

    /**
     * 方法名: ControllerHelper.static initializer
     * 作者: LSL
     * 创建时间: 10:49 2018\5\9 0009
     * 描述: 初始化请求与映射关系
     */
    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (controllerClassSet != null && controllerClassSet.size() > 0) {
            for (Class<?> aClass : controllerClassSet) {
                Controller con = aClass.getAnnotation(Controller.class);
                String path = con.value();
                Method[] methods = aClass.getDeclaredMethods();
                if (methods != null && methods.length > 0) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(GET.class)) {
                            GET get = method.getAnnotation(GET.class);
                            String mapping = get.value();
                            String url = urlDual(path, mapping);
                            if (url.matches("^[/[\\w]+]+[/]?")) {
                                Request request = new Request("get", url);
                                Handler handler = new Handler(aClass, method);
                                ACTION_MAP.put(request, handler);
                                log.debug("[get,\"{}\"] bound to {}.{}()", url, aClass.getName(), method.getName());
                            }
                        } else if (method.isAnnotationPresent(POST.class)) {
                            POST post = method.getAnnotation(POST.class);
                            String mapping = post.value();
                            String url = urlDual(path, mapping);
                            if (url.matches("^[/[\\w]+]+[/]?")) {
                                Request request = new Request("post", url);
                                Handler handler = new Handler(aClass, method);
                                ACTION_MAP.put(request, handler);
                                log.debug("[post,\"{}\"] bound to {}.{}()", url, aClass.getName(), method.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    public static String urlDual(String controller, String method) {
        controller = "/" + controller + "/";
        method = "/" + method;
        String url = controller + method;
        Pattern compile = Pattern.compile("[/]+");
        Matcher matcher = compile.matcher(url);
        return matcher.replaceAll("/");
    }

    public static Handler getHandler(String requsetMethod, String requestPath) {
        Request request = new Request(requsetMethod, requestPath);
        return ACTION_MAP.get(request);
    }

    public static void changeHandler(Request source, Request targe) {
        Handler handler = ACTION_MAP.remove(source);
        ACTION_MAP.put(targe, handler);
    }
}
