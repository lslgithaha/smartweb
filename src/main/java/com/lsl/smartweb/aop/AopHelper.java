package com.lsl.smartweb.aop;


import com.lsl.smartweb.annotion.Aspect;
import com.lsl.smartweb.annotion.Service;
import com.lsl.smartweb.aop.interceptor.AspectProxy;
import com.lsl.smartweb.aop.core.Proxy;
import com.lsl.smartweb.aop.proxy.ProxyManager;
import com.lsl.smartweb.db.TransactionProxy;
import com.lsl.smartweb.core.BeanHelper;
import com.lsl.smartweb.core.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Create by LSL on 2018\5\18 0018
 * 描述：
 * 版本：1.0.0
 */
public final class AopHelper {
    private static final Logger log = LoggerFactory.getLogger(AopHelper.class);
    static{
        try{
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Class<?> c : targetMap.keySet()) {
                List<Proxy> proxyList = targetMap.get(c);
                Object proxy = ProxyManager.getProxy(c, proxyList);
                //初始化代理框架，并且将原有的bean覆盖
                BeanHelper.setBean(c,proxy);
            }
        }catch (Exception e){
            log.error(" aop 切面框架初始化失败！错误信息："+e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 方法名: AopHelper.createTargetClassSet
     * 作者: LSL
     * 创建时间: 13:53 2018\5\21 0021
     * 描述: 获取所有有切面注解的类
     * 参数: [aspect]
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect)throws Exception{
        HashSet<Class<?>> classes = new HashSet<>();
        Class<? extends Annotation> value = aspect.value();
        if(value != null && !value.equals(Aspect.class)){
            classes.addAll(ClassHelper.getClassSetByAnnotation(value));
        }
        return classes;
    }

    /**
     * 方法名: AopHelper.createProxyMap
     * 作者: LSL
     * 创建时间: 9:41 2018\5\22 0022
     * 描述: 获得切面对应的需被代理类
     * 参数: []
     * 返回: java.util.Map<java.lang.Class<?>,java.util.Set<java.lang.Class<?>>>
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        HashMap<Class<?>, Set<Class<?>>> classSetHashMap = new HashMap<>();
        Set<Class<?>> classSetBySuper = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> c : classSetBySuper) {
            if(c.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = c.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                classSetHashMap.put(c, targetClassSet);
            }
        }
        //加入事务代理的映射关系
        classSetHashMap.put(TransactionProxy.class,ClassHelper.getClassSetByAnnotation(Service.class));
        return  classSetHashMap;
    }
    /**
     * 方法名: AopHelper.createTargetMap
     * 作者: LSL
     * 创建时间: 9:51 2018\5\22 0022
     * 描述: 建立代理类目标类的映射关系
     * 参数: [proxyMap]
     * 返回: java.util.Map<java.lang.Class<?>,java.util.List<com.lovely.aop.aspect.Proxy>>
     */
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws  Exception{
        HashMap<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()) {
            Class<?> key = entry.getKey();//代理类
            Set<Class<?>> value = entry.getValue();//需要被代理类set集合
            for (Class<?> aClass : value) {
                Proxy proxy= (Proxy) key.newInstance();//代理内的实例
                if(targetMap.containsKey(aClass)){
                    targetMap.get(aClass).add(proxy);
                }else{
                    List<Proxy> proxies = new ArrayList<>();
                    proxies.add(proxy);
                    targetMap.put(aClass,proxies);
                }
            }
        }
        return targetMap;
    }
}
