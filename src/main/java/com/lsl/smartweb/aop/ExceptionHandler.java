package com.lsl.smartweb.aop;

import com.lsl.smartweb.aop.core.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface ExceptionHandler {
        /**
         * 方法名: ExceptionHandler.resolveException_Json
         * 作者: LSL
         * 创建时间: 15:55 2018\6\25 0025
         * 描述: 异常处理方法json
         * 参数: [request, response, parm, exception]
         * 返回: 跳转的页面
         */
        Object resolveException_Json(HttpServletRequest request,
                                     HttpServletResponse response,
                                     HashMap<String, Object> parm,
                                     Exception exception);
        /**
         * 方法名: ExceptionHandler.resolveException_View
         * 作者: LSL
         * 创建时间: 15:55 2018\6\25 0025
         * 描述: 异常处理方法页面跳转
         * 参数: [request, response, parm, exception]
         * 返回: 对象
         */
        Object resolveException_View(HttpServletRequest request,
                                     HttpServletResponse response,
                                     HashMap<String, Object> parm,
                                     Exception exception);
        /**
         * 方法名: ExceptionHandler.resolveException_Method_not_define
         * 作者: LSL
         * 创建时间: 9:19 2018\7\23 0023
         * 描述: 处理接口未找到的返回提示
         * 参数: [path]
         * 返回: java.lang.Object
         */
        Object resolveException_Method_not_define(Handler handler);


}
