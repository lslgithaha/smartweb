package com.lsl.smartweb.db.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Create by LSL on 2018\6\22 0022
 * 描述：
 * 版本：1.0.0
 */
public class PoolManage extends Thread{
    private static final Logger log = LoggerFactory.getLogger(PoolManage.class);
    static int refreshTime = 0;
    static String refreshsql = null;
    private static Pool pool =null;
    static int minConnect = 0;
    static int connectCount = 0;
    static int maxConnect = 0;
    static int overtime = 0;
    public PoolManage(){
        pool = new Pool(overtime,minConnect,connectCount,maxConnect);
        pool.init();
        this.start();
    }
    @Override
    public void run() {
        //连接刷新线程
        log.info("刷新池连接线程启动！");
        while (true){
            try {
                sleep(refreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pool.delConn();
            for(int i=0;i<pool.getPool().size();i++){
                try {
                    Connection connection = pool.get();
                    log.debug("刷新连接：{}",connection.toString());
                    connection.createStatement().execute(refreshsql);
                    pool.set(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                    log.error("刷新连接出错：{}",e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("刷新连接出错：{}",e);
                }
            }
        }
    }
    public Connection getConnection(){
        try {
            return pool.get();
        } catch (InterruptedException e) {
        }
        return null;
    }

    public void closeConnection(Connection connection) {
        pool.set(connection);
    }


    public static void setRefreshsql(String refreshsql) {
        PoolManage.refreshsql = refreshsql;
    }
    public static void setRefreshTime(int refreshTime) {
        PoolManage.refreshTime = refreshTime;
    }
    public static void setMinConnect(int minConnect) {
        PoolManage.minConnect = minConnect;
    }

    public static void setMaxConnect(int maxConnect) {
        PoolManage.maxConnect = maxConnect;
    }

    public static void setOvertime(int overtime) {
        PoolManage.overtime = overtime;
    }
}
