package com.lsl.smartweb.db.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Create by LSL on 2018\6\22 0022
 * 描述：
 * 版本：1.0.0
 */
public class Pool {

    private static LinkedList<Connection> pool = new LinkedList<>();
    long overtime = 0;
    int minConnect = 0;
    int connectCount = 0;
    int maxConnect = 0;
    private static final Logger log = LoggerFactory.getLogger(Pool.class);

    public Pool(long overtime, int minConnect, int connectCount, int maxConnect) {
        this.overtime = overtime;
        this.minConnect = minConnect;
        this.connectCount = connectCount;
        this.maxConnect = maxConnect;
    }

    void init() {
        while (connectCount < minConnect){
            addConn();
        }
    }

    public static LinkedList<Connection> getPool() {
        return pool;
    }
    Connection get() throws InterruptedException {
        synchronized (pool) {
            if (pool.size() == 0) {
                if (connectCount < maxConnect) {
                    addConn();
                    return get();
                } else {
                    long l = System.currentTimeMillis();
                    pool.wait(overtime);
                    if(l+overtime < System.currentTimeMillis()){
                        log.info("从连接池取链接超时！");
                        return null;
                    }
                    return get();
                }
            } else {
                Connection connection = pool.removeFirst();
                log.debug("连接池中取出了一个连接，当前总共{}个连接，池里面还有{}个连接", connectCount, pool.size());
                return connection;
            }
        }
    }

    void set(Connection connection) {
        synchronized (pool) {
            pool.addLast(connection);
            pool.notifyAll();
            log.debug("连接池中添加了一个连接，当前总共{}个连接，池里面还有{}个连接", connectCount, pool.size());
        }
    }

    void delConn(){
        synchronized (pool) {
           if(pool.size()>minConnect){
               for (int i = 0; i <pool.size()-minConnect ; i++) {
                   try {
                       delConn(get());
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
        }
    }
    void delConn(Connection connection){
        try {
            if(connection==null) return;
            log.debug("断开了一个连接：{}",connection.toString());
            ConectFactory.closeConnevtion(connection);

        } catch (SQLException e) {
            log.error("关闭连接发生异常",e);
        }finally {
            connectCount--;
        }
    }
    void addConn(){
        try {
            Connection connection = ConectFactory.createConnection();
            if(connection !=null){
                connectCount++;
                set(connection);
            }
        } catch (Exception e) {
            log.error("连接创建异常",e);
        }
    }
    void closeAll(){
        synchronized (pool){
            for (int i = 0; i < pool.size(); i++) {
                delConn(pool.get(i));
            }
        }
    }
}
