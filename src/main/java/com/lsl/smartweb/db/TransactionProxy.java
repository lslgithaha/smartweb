package com.lsl.smartweb.db;

import com.lsl.smartweb.annotion.Transaction;
import com.lsl.smartweb.aop.core.Proxy;
import com.lsl.smartweb.aop.proxy.ProxyChain;
import com.lsl.smartweb.configure.SmartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by LSL on 2018\5\25 0025
 * 描述：事务代理
 * 版本：1.0.0
 */
public class TransactionProxy implements Proxy {
    private static final Logger log = LoggerFactory.getLogger(TransactionProxy.class);
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
      protected Boolean initialValue(){
          return false;
      }
    };
    private static final Pattern pattern = Pattern.compile(SmartConfig.getTransaction());
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object object;
        boolean flag = FLAG_HOLDER.get();
        Method targetMethod = proxyChain.getTargetMethod();
        String name = targetMethod.getName();
        Matcher matcher = pattern.matcher(name);
        boolean rs = matcher.matches();
        if(!flag && (targetMethod.isAnnotationPresent(Transaction.class))||rs){
            FLAG_HOLDER.set(true);
            try {
                DbManage.beginTransaction();
                log.debug("开启事务...");
                object = proxyChain.doProxyChain();
                DbManage.commitTransaction();
                log.debug("事务提交...");

            } catch (Exception e) {
                log.debug("事务回滚");
                DbManage.rollbackTransaction();
                log.error("发生一个错误：",e);
                e.printStackTrace();
                throw e;
            } finally {
                FLAG_HOLDER.remove();
            }
        }else{
            object = proxyChain.doProxyChain();
            DbManage.closeConnection();
        }
        return object;
    }
}
