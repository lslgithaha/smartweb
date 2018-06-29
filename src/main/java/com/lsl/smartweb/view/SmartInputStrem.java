package com.lsl.smartweb.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by LSL on 2018\6\29 0029
 * 描述：
 * 版本：1.0.0
 */
public class SmartInputStrem{
    private InputStream inputStream;
    private String filename;
    private boolean isDownload ;

    private SmartInputStrem(InputStream inputStream, String filename, boolean isDownload) {
        this.inputStream = inputStream;
        this.filename = filename==null?"":filename;
        this.isDownload = isDownload;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isDownload() {
        return isDownload;
    }
    /**
     * 方法名: SmartInputStrem.createDownLoad
     * 作者: LSL
     * 创建时间: 17:26 2018\6\29 0029
     * 描述: 跟据输入流创建文件下载
     * 参数: [in, filename]
     * 返回: com.lsl.smartweb.view.SmartInputStrem
     */
    public static SmartInputStrem createDownLoad(InputStream in,String filename){
        return new SmartInputStrem(in,filename,true);
    }
    /**
     * 方法名: SmartInputStrem.createDownLoad
     * 作者: LSL
     * 创建时间: 17:26 2018\6\29 0029
     * 描述: 跟据输入流创建文件查看,根据文件命设置响应头
     * 参数: [in, filename]
     * 返回: com.lsl.smartweb.view.SmartInputStrem
     */
    public static SmartInputStrem createFileReturn(InputStream in,String filename){
        return new SmartInputStrem(in,filename,false);
    }
    /**
     * 方法名: SmartInputStrem.createDownLoad
     * 作者: LSL
     * 创建时间: 17:26 2018\6\29 0029
     * 描述: 跟据输入流创建文件查看
     * 参数: [in, filename]
     * 返回: com.lsl.smartweb.view.SmartInputStrem
     */
    public static SmartInputStrem createFileReturn(InputStream in){
        return createFileReturn(in,null);
    }
}
