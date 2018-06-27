package text.lsl.smart;

import com.lsl.smartweb.annotion.Dao;
import com.lsl.smartweb.annotion.Inter;
import com.lsl.smartweb.annotion.Service;
import com.lsl.smartweb.annotion.Transaction;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.sql.SQLException;

/**
 * Create by LSL on 2018\6\22 0022
 * 描述：
 * 版本：1.0.0
 */
@Service
public class TestService {
    @Inter
    private dao d;
    @Transaction
    public String hehe(String xx) throws SQLException {
        d.ss(xx);
        d.ss(xx);
        return "servie"+xx;
    }
}
