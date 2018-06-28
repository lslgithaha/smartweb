package com.lsl.smartweb.db;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Create by LSL on 2018\6\28 0028
 * 描述：
 * 版本：1.0.0
 */
@WebListener
public class ShutdownListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("容器关闭：销毁所有数据连接！");
        DbManage.closeAll();
    }
}
