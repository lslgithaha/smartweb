package com.lsl.smartweb.aop.proxy;

import com.lsl.smartweb.aop.core.Proxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Create by LSL on 2018\5\18 0018
 * 描述：
 * 版本：1.0.0
 */
public class ProxyManager {
    /**
     * 方法名: ProxyManager.getMethodInterceptor
     * 作者: LSL
     * 创建时间: 16:31 2018\5\18 0018
     * 描述: 获得方法拦截接口实例
     * 参数: [cls, proxyList]
     * 返回: net.sf.cglib.proxy.MethodInterceptor
     */
    private static MethodInterceptor getMethodInterceptor(final Class<?> cls,final List<Proxy> proxyList) {
        return (
                (Object o, Method method, Object[] objects, MethodProxy methodProxy)
                        -> {
                    return new ProxyChain(cls,o,method,methodProxy,objects,proxyList).doProxyChain();
                });
    }
    /**
     * 方法名: ProxyManager.getProxy
     * 作者: LSL
     * 创建时间: 16:32 2018\5\18 0018
     * 描述: 获得代理
     * 参数: [cls, proxyList]
     * 返回: T
     */
    public  static <T> T getProxy(final Class<T> cls,final List<Proxy> proxyList) {
        return (T) Enhancer.create(cls, getMethodInterceptor(cls,proxyList));
    }
}
