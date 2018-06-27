package text.lsl.smart;

import com.lsl.smartweb.annotion.Dao;
import com.lsl.smartweb.db.DbManage;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Create by LSL on 2018\6\25 0025
 * 描述：
 * 版本：1.0.0
 */
@Dao
public class dao {
    public void ss(String xx) throws SQLException {
        String sql="insert into user_info (id,name) values('5','jjjjj')";
        Connection conn = DbManage.getConn();
        conn.createStatement().execute(sql);
    }
}
