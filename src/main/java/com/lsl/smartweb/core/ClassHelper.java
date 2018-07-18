package com.lsl.smartweb.core;

import com.lsl.smartweb.annotion.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create by LSL on 2018\5\8 0008
 * 描述：用于帮助类操作
 * 版本：1.0.0
 */
public final class ClassHelper {
    private static Set<Class<?>> CLASS_SET;
    /**
     * 方法名: ClassHelper.static initializer
     * 作者: LSL
     * 创建时间: 17:39 2018\5\8 0008
     * 描述: 初始化类集合
     * 参数:
     * 返回:
     */
    static {
        String basePack = "";
        CLASS_SET = ClassUtils.getClassSet(basePack);
    }
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }
    /**
     * 方法名: ClassHelper.getClsSet
     * 作者: LSL
     * 创建时间: 17:49 2018\5\8 0008
     * 描述: 根据注解类型获取类集合
     * 参数: [classType]
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    public static Set<Class<?>> getClsSet(Class classType){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls:CLASS_SET) {
            if(classType ==null ){
                if(cls.getAnnotations()!=null && cls.getAnnotations().length>0 ){
                    classSet.add(cls);
                }
            }else {
                if(cls.isAnnotationPresent(classType) ){
                    classSet.add(cls);
                }
            }
        }
        return classSet;
    }
    /**
     * 方法名: ClassHelper.getControllerClassSet
     * 作者: LSL
     * 创建时间: 18:00 2018\5\8 0008
     * 描述: 获取Controller
     * 参数: []
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    public static Set<Class<?>> getControllerClassSet(){
        return getClsSet(Controller.class);
    }
    /**
     * 方法名: ClassHelper.getServiceClassSet
     * 作者: LSL
     * 创建时间: 18:01 2018\5\8 0008
     * 描述: 获取Service
     * 参数: []
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    public static Set<Class<?>> getServiceClassSet(){
        return getClsSet(Service.class);
    }
    /**
     * 方法名: ClassHelper.getBeanClassSet
     * 作者: LSL
     * 创建时间: 18:01 2018\5\8 0008
     * 描述: 获取所有有注解的class
     * 参数: []
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> set =
        CLASS_SET.parallelStream().filter(e->e.getName().startsWith("com.lsl.smartweb.annotion")).collect(Collectors.toSet());
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> aClass : set) {
            classSet.addAll(getClsSet(aClass));
        }
        return classSet;
    }
    /**
     * 方法名: ClassHelper.getClassSetBySuper
     * 作者: LSL
     * 创建时间: 17:11 2018\5\18 0018
     * 描述: 获得一个类在容器中所有的子类
     * 参数: [superClass]
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
        HashSet<Class<?>> set = new HashSet<>();
        for (Class<?> aClass : CLASS_SET) {
            if(superClass.isAssignableFrom(aClass) && !superClass.equals(aClass)){
                set.add(aClass);
            }
        }
        return set;
    }
    /**
     * 方法名: ClassHelper.getClassSetBySuper
     * 作者: LSL
     * 创建时间: 17:11 2018\5\18 0018
     * 描述: 获得容器中带有某注解的类
     * 参数: [superClass]
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> anno){
        HashSet<Class<?>> set = new HashSet<>();
        for (Class<?> aClass : CLASS_SET) {
            if(aClass.isAnnotationPresent(anno)){
                set.add(aClass);
            }
        }
        return set;
    }

}
