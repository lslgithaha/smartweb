package com.lsl.smartweb.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法名: Dao
 * 作者: LSL
 * 创建时间: 14:42 2018\6\25 0025
 * 描述: dao层注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {
}
