package com.lsl.smartweb.core;


import com.lsl.smartweb.aop.AopHelper;
import com.lsl.smartweb.aop.core.ControllerHelper;
import com.lsl.smartweb.db.DbManage;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：
 * 版本：1.0.0
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] classList = {ClassHelper.class,BeanHelper.class,AopHelper.class, InterHelper.class,ControllerHelper.class, DbManage.class};
        for (Class<?> cls : classList) {
            ClassUtils.loadClass(cls.getName(),true);
        }
    }
}
