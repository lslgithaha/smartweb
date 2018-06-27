package com.lsl.smartweb.aop.core;

import java.lang.reflect.Method;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：处理对象
 * 版本：1.0.0
 */
public class Handler {
    private Class<?> contriller;
    private Method method;

    public Handler(Class<?> contriller, Method method) {
        this.contriller = contriller;
        this.method = method;
    }

    public Class<?> getContriller() {
        return contriller;
    }

    public Method getMethod() {
        return method;
    }
}
