package com.lsl.smartweb.db.execsql;

import com.lsl.smartweb.db.DbManage;
import com.lsl.smartweb.utils.Util;
import jdk.nashorn.internal.ir.ReturnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Create by LSL on 2018\6\29 0029
 * 描述：sql执行模板
 * 版本：1.0.0
 */
public class SqlTemple {
    private static final Logger log = LoggerFactory.getLogger(SqlTemple.class);

    public static int exec(String sql, Object[] val) throws SQLException {
        return updateExec(sql, Arrays.asList(val));
    }

    public static int exec(String sql) throws SQLException {
        return updateExec(sql, null);
    }

    public static boolean execboolean(String sql, Object[] val) throws SQLException {
        return updateExec(sql, Arrays.asList(val)) > 0;
    }

    public static boolean execboolean(String sql) throws SQLException {
        return updateExec(sql, null) > 0;
    }



    /**
     * 方法名: SqlTemple.queryToList
     * 作者: LSL
     * 创建时间: 21:18 2018\6\29 0029
     * 描述:返回二维数组
     * 参数: [sql, val, type]
     * 返回: java.util.List<T>
     */
    public static <T> List<T> queryToList(String sql, List val, Class<T> type) throws SQLException {
        List<Map<String, Object>> list = queryExec(sql, val);
        List<T> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            result.add(Util.toBean(map, type));
        }
        return result;
    }
    public static <T> List<T> queryToList(String sql, Class<T> type) throws SQLException {
        return queryToList(sql,null,type);
    }

    /**
     * 方法名: SqlTemple.queryToArray2
     * 作者: LSL
     * 创建时间: 21:33 2018\6\29 0029
     * 描述: 返回二维数组
     * 参数: [sql, val]
     * 返回: java.lang.String[][]
     */
    public static String[][] queryToArray2(String sql, List val) throws SQLException {
        return queryExec(sql, val,"");
    }
    public static String[][] queryToArray2(String sql) throws SQLException {
        return queryToArray2(sql,null);
    }
    /**
     * 方法名: SqlTemple.queryToArray
     * 作者: LSL
     * 创建时间: 21:17 2018\6\29 0029
     * 描述: 返回数组
     * 参数: [sql, val, type]
     * 返回: T[]
     */
    public static <T> T[] queryToArray(String sql, List val, Class<T> type) throws SQLException {
        List<T> list = queryToList(sql, val, type);
        return (T[]) list.toArray(new Object[0]);
    }
    public static <T> T[] queryToArray(String sql, Class<T> type) throws SQLException {
        return queryToArray(sql,null,type);
    }

    /**
     * 方法名: SqlTemple.query
     * 作者: LSL
     * 创建时间: 21:26 2018\6\29 0029
     * 描述: 查询一个实体
     * 参数: [sql, val, type]
     * 返回: T
     */
    public static <T>T query(String sql, List val,Class<T> type) throws SQLException, SqlTempleException {
        try {
            List<T> list = queryToList(sql, val, type);
            if(list.size() == 0 )return null;
            if(list.size() == 1)return list.get(0);
            throw new SqlTempleException("返回的数据不止一条！");
        } catch (SQLException e) {
           throw e;
        } catch (SqlTempleException e) {
            log.error("查询单个实体时返回的数据不止一条:",e);
            throw e;
        }
    }
    public static <T>T query(String sql, Class<T> type) throws SQLException, SqlTempleException {
        return query(sql,null,type);
    }

    /**
     * 方法名: SqlTemple.queryExec
     * 作者: LSL
     * 创建时间: 21:16 2018\6\29 0029
     * 描述: 执行sql
     * 参数: [sql, val]
     * 返回: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    private static List<Map<String, Object>> queryExec(String sql, List<Object> val) throws SQLException {
        String psql = getPreparedSQL(sql, val);
        Connection conn = DbManage.getConn();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            if (val != null) {
                for (int i = 1; i <= val.size(); i++) {
                    pst.setObject(i, val.get(i - 1));
                }
            }
            rs = pst.executeQuery();
            log.debug("执行sql：{} 完成！", psql);
            List<Map<String, Object>> list = convertList(rs);
            return list;
        } catch (SQLException e) {
//            e.printStackTrace();
            log.error("执行sql：{} 发生错误！", psql, e);
            throw e;
        } finally {
            if (pst != null) pst.close();
            if (rs != null) {
                rs.close();
            }
        }
    }
    /**
     * 方法名: SqlTemple.queryExec
     * 作者: LSL
     * 创建时间: 21:16 2018\6\29 0029
     * 描述: 执行sql
     * 参数: [sql, val]
     * 返回: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    private static String[][] queryExec(String sql, List<Object> val,String arrau ) throws SQLException {
        String psql = getPreparedSQL(sql, val);
        Connection conn = DbManage.getConn();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            if (val != null) {
                for (int i = 1; i <= val.size(); i++) {
                    pst.setObject(i, val.get(i - 1));
                }
            }
            rs = pst.executeQuery();
            log.debug("执行sql：{} 完成！", psql);
            ResultSetMetaData rsmd = rs.getMetaData() ;
            int columnCount = rsmd.getColumnCount();
            ArrayList<String[]> jg=new ArrayList<>();
            while(rs.next()){
                String[] a=new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    a[j] = rs.getString(j+1);
                }
                jg.add(a);
            }
            return jg.toArray(new String[0][0]);
        } catch (SQLException e) {
//            e.printStackTrace();
            log.error("执行sql：{} 发生错误！", psql, e);
            throw e;
        } finally {
            if (pst != null) pst.close();
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 方法名: SqlTemple.convertList
     * 作者: LSL
     * 创建时间: 21:16 2018\6\29 0029
     * 描述: 结果转换
     * 参数: [rs]
     * 返回: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }

    /**
     * 方法名: SqlTemple.updateExec
     * 作者: LSL
     * 创建时间: 21:17 2018\6\29 0029
     * 描述: 行修改相关语句
     * 参数: [sql, val]
     * 返回: int
     */
    private static int updateExec(String sql, List val) throws SQLException {
        String psql = getPreparedSQL(sql, val);
        Connection conn = DbManage.getConn();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            if (val != null) {
                for (int i = 1; i <= val.size(); i++) {
                    pst.setObject(i, val.get(i - 1));
                }
            }
            int rs = pst.executeUpdate();
            log.debug("执行sql：{} 完成！", psql);
            return rs;
        } catch (SQLException e) {
//            e.printStackTrace();
            log.error("执行sqlw：{} 发生错误！", psql, e);
            throw e;
        }
    }

    /**
     * 获得PreparedStatement向数据库提交的SQL语句
     *
     * @param sql
     * @param params
     * @return
     */
    private static String getPreparedSQL(String sql, List<Object> params) {//1 如果没有参数，说明是不是动态SQL语句
        int paramNum = 0;
        if (null != params) paramNum = params.size();
        if (1 > paramNum) return sql;
        //2 如果有参数，则是动态SQL语句
        StringBuffer returnSQL = new StringBuffer();
        String[] subSQL = sql.split("\\?");
        for (int i = 0; i < paramNum; i++) {
            returnSQL.append(subSQL[i]).append(" '").append(params.get(i)).append("' ");
        }

        if (subSQL.length > params.size()) {
            returnSQL.append(subSQL[subSQL.length - 1]);
        }
        return returnSQL.toString();
    }

}
