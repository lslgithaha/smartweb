package com.lsl.smartweb.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by LSL on 2018\5\8 0008
 * 描述：controller注解
 * 版本：1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    /**
     * 方法名: Action.value
     * 作者: LSL
     * 创建时间: 17:30 2018\5\8 0008
     * 描述: 请求类型与路径
     * 参数: []
     * 返回: java.lang.String
     */
    String value() default "";
}
