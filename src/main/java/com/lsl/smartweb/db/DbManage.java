package com.lsl.smartweb.db;

import com.lsl.smartweb.configure.SmartConfig;
import com.lsl.smartweb.db.pool.PoolManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Create by LSL on 2018\5\25 0025
 * 描述：数据操作工具类
 * 版本：1.0.0
 */
public class DbManage {
    private static final Logger log = LoggerFactory.getLogger(DbManage.class);
    private static PoolManage connPoolManage;
    private static ThreadLocal<Connection> conns = new ThreadLocal<>();
    static{
        try {
            connPoolManage= new PoolManage();
        } catch (Exception e) {
            log.error("数据源初始化异常",e);
            e.printStackTrace();
        }
    }
    /**
     * 方法名: DbManage.getConn
     * 作者: LSL
     * 创建时间: 18:05 2018\5\25 0025
     * 描述: 获取数据库连接
     * 参数: []
     * 返回: java.sql.Connection
     */
    public static Connection getConn(){
        Connection connection = conns.get();
        try {
            if(null == connection){
                connection = connPoolManage.getConnection();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conns.set(connection);
        }
        return connection;
    }
    /**
     * 方法名: DbManage.closeConnection
     * 作者: LSL
     * 创建时间: 18:05 2018\5\25 0025
     * 描述: 关闭数据库连接
     * 参数: []
     * 返回: void
     */
    public static void closeConnection(){
        Connection connection = conns.get();
        try {
            if(connection !=null){
                connPoolManage.closeConnection(connection);
            }
        }finally {
            conns.remove();
        }
    }
    /**
     * 方法名: DbManage.beginTransaction
     * 作者: LSL
     * 创建时间: 18:06 2018\5\25 0025
     * 描述: 开启事务
     * 参数: []
     * 返回: void
     */
    public static void beginTransaction(){
        Connection conn = getConn();
        if(null != conn){
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                rollbackTransaction();
                e.printStackTrace();
            }finally {
                conns.set(conn);
            }
        }
    }
    /**
     * 方法名: DbManage.commitTransaction
     * 作者: LSL
     * 创建时间: 18:06 2018\5\25 0025
     * 描述: 提交事务
     * 参数: []
     * 返回: void
     */
    public static  void commitTransaction(){
        Connection conn = getConn();
        try {
            if(conn != null){
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 方法名: DbManage.rollbackTransaction
     * 作者: LSL
     * 创建时间: 18:06 2018\5\25 0025
     * 描述: 回滚事务
     * 参数: []
     * 返回: void
     */
    public static void rollbackTransaction(){
        Connection conn = getConn();
        try {
            if(null != conn){
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void closeAll(){
        connPoolManage.closeAll();
    }
}
