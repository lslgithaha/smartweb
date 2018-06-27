package com.lsl.smartweb.aop.interceptor;

import com.lsl.smartweb.aop.core.Proxy;
import com.lsl.smartweb.aop.proxy.ProxyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Create by LSL on 2018\5\18 0018
 * 描述：
 * 版本：1.0.0
 */
public abstract class AspectProxy implements Proxy {
    private static final Logger log = LoggerFactory.getLogger(AspectProxy.class);
    /**
     * 方法名: AspectProxy.doProxy
     * 作者: LSL
     * 创建时间: 17:04 2018\5\18 0018
     * 描述: 代理执行
     * 参数: [proxyChain]
     * 返回: java.lang.Object
     */
    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result=null;
        Class<?> cls = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();
        begin();
        try {
            if(needIntercept(cls,targetMethod,methodParams)){
                before(cls,targetMethod,methodParams);
                result = proxyChain.doProxyChain();
                after(cls,targetMethod,methodParams);
            }else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            log.error("proxy error,msg:{}",e.getMessage());
            error(cls,targetMethod,methodParams,e);
            throw e;
        } finally {
            end();
        }
        return result;
    }

    /**
     * 方法名: AspectProxy.begin
     * 作者: LSL
     * 创建时间: 17:05 2018\5\18 0018
     * 描述: 一开始就执行
     * 参数: []
     * 返回: void
     */
    public void begin(){

    }
    /**
     * 方法名: AspectProxy.intercept
     * 作者: LSL
     * 创建时间: 17:05 2018\5\18 0018
     * 描述: 判定是否需要拦截
     * 参数: [cls, method, params]
     * 返回: boolean
     */
    public boolean needIntercept(Class<?> cls,Method method,Object[] params) throws Throwable{
        return true;
    }
    /**
     * 方法名: AspectProxy.before
     * 作者: LSL
     * 创建时间: 17:06 2018\5\18 0018
     * 描述: 在需要拦截的方法前执行
     * 参数: [cls, method, params]
     * 返回: void
     */
    public void before(Class<?> cls,Method method,Object[] params) throws Throwable{

    }
    /**
     * 方法名: AspectProxy.after
     * 作者: LSL
     * 创建时间: 17:06 2018\5\18 0018
     * 描述: 在需要拦截的方法后执行
     * 参数: [cls, method, params]
     * 返回: void
     */
    public void after(Class<?> cls,Method method,Object[] params) throws Throwable{

    }
    /**
     * 方法名: AspectProxy.error
     * 作者: LSL
     * 创建时间: 17:06 2018\5\18 0018
     * 描述: 发生异常时执行
     * 参数: [cls, method, params, e]
     * 返回: void
     */
    public void error(Class<?> cls,Method method,Object[] params,Exception e) throws Throwable{

    }
    /**
     * 方法名: AspectProxy.end
     * 作者: LSL
     * 创建时间: 17:07 2018\5\18 0018
     * 描述: 最后执行
     * 参数: []
     * 返回: void
     */
    public void end(){

    }
}
