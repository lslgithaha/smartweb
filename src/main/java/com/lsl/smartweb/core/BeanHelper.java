package com.lsl.smartweb.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：豆子操作间
 * 版本：1.0.0
 */
public class BeanHelper {

    /**
     * 作者: LSL
     * 创建时间: 9:24 2018\5\9 0009
     * 描述:存放bean
     */
    private static final Map<Class<?>,Object> BEAN_MAP= new HashMap<Class<?>,Object>();

    /**
     * 方法名: BeanHelper.static initializer
     * 作者: LSL
     * 创建时间: 9:42 2018\5\9 0009
     * 描述: 初始化bean map
     */
    static {
        Set<Class<?>> beans = ClassHelper.getBeanClassSet();
        for (Class<?> bean : beans) {
            Object o = BeanFactory.newInstance(bean);
            BEAN_MAP.put(bean,o);
        }
    }
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 方法名: BeanHelper.getBean
     * 作者: LSL
     * 创建时间: 9:47 2018\5\9 0009
     * 描述: 获取一个对象
     * 参数: [cls]
     * 返回: T
     */
    public static<T> T getBean(Class<?> cls){
        if(BEAN_MAP.containsKey(cls)){
            return  (T)BEAN_MAP.get(cls);
        }
        throw new RuntimeException("can not find bean class : "+cls.getName());
    }
    /**
     * 方法名: BeanHelper.serBean
     * 作者: LSL
     * 创建时间: 17:03 2018\5\18 0018
     * 描述: 设置beanmap映射
     * 参数: [cls, obj]
     * 返回: void
     */
    public static void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }
}
