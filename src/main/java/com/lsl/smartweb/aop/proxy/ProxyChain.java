package com.lsl.smartweb.aop.proxy;

import com.lsl.smartweb.aop.core.Proxy;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by LSL on 2018\5\18 0018
 * 描述：
 * 版本：1.0.0
 */
public class ProxyChain {
    private Class<?> targetClass;
    private Object targetObject;
    private Method targetMethod;
    private MethodProxy methodProxy;
    private Object[] methodParams;
    private List<Proxy> proxyList = new ArrayList<>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    /**
     * 方法名: ProxyChain.doProxyChain
     * 作者: LSL
     * 创建时间: 16:24 2018\5\18 0018
     * 描述: 执行代理栈
     * 参数: []
     * 返回: java.lang.Object
     */
    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;
    }
}
