package com.lsl.smartweb.fileup;

import com.lsl.smartweb.annotion.Controller;
import com.lsl.smartweb.annotion.GET;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by LSL on 2018\6\27 0027
 * 描述：
 * 版本：1.0.0
 */
@Controller
public class ProcessController {
    private static final Map<String,UpStatus> STATUS_MAP = new HashMap<>();
    @GET("/fileupload_process")
    public UpStatus getProccess(HttpSession session){
//        System.out.println(session.getId());
//        System.out.println(STATUS_MAP.get(session.getId()));
        return STATUS_MAP.get(session.getId());
    }

    public final static void  setStatus(String sessionId,UpStatus upStatus){
        STATUS_MAP.put(sessionId,upStatus);
    }
}
