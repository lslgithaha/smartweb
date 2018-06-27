package com.lsl.smartweb.annotion;

import java.lang.annotation.*;

/**
 * 作者: LSL
 * 创建时间: 16:48 2018\5\18 0018
 * 描述: 注解切面代理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
