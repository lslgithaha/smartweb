package com.lsl.smartweb.db.pool;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Create by LSL on 2018\6\22 0022
 * 描述：
 * 版本：1.0.0
 */
public class ConectFactory {
    private static  String driver = null;
    private static  String url = null;
    private static  String user = null;
    private static  String password = null;

    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        return DriverManager.getConnection(url,user,password);

    }
    public static void closeConnevtion(Connection connection) throws SQLException {
        connection.close();
    }

    public static void setDriver(String driver) {
        ConectFactory.driver = driver;
    }

    public static void setUrl(String url) {
        ConectFactory.url = url;
    }

    public static void setUser(String user) {
        ConectFactory.user = user;
    }

    public static void setPassword(String password) {
        ConectFactory.password = password;
    }
}
