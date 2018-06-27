package text.lsl.smart;

import com.lsl.smartweb.annotion.Controller;
import com.lsl.smartweb.annotion.GET;
import com.lsl.smartweb.annotion.Inter;
import com.lsl.smartweb.annotion.POST;
import com.lsl.smartweb.fileup.SmartFile;
import jdk.nashorn.internal.runtime.logging.Loggable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.SQLException;


/**
 * Create by LSL on 2018\6\22 0022
 * 描述：
 * 版本：1.0.0
 */
@Controller
public class TeconController{
    @Inter
    private TestService testService;

    @POST("/test")
    public String og(SmartFile haha, String hehe, HttpSession session) throws SQLException {
        haha.write("E:\\"+hehe+".zip");
        return session.getId();
    }
    @GET("/void")
    public void ot(HttpServletRequest request){
        System.out.println(request.getRequestURI());
    }
}
