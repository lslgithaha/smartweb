package com.lsl.smartweb.aop.interceptor;


import com.lsl.smartweb.annotion.Aspect;
import com.lsl.smartweb.annotion.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Create by LSL on 2018\5\18 0018
 * 描述：
 * 版本：1.0.0
 */@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    private static final Logger log = LoggerFactory.getLogger(ControllerAspect.class);


    @Override
    public void begin() {

    }
    @Override
    public void end() {

    }
    private long begin;
    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        log.debug("class:{} , method:{} ",cls.getName(),method.getName());
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
        log.debug("time:{}",System.currentTimeMillis()-begin);
    }

}
