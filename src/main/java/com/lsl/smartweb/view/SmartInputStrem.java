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

    private SmartInputStrem(InputStream inputStream, String filename) {
        this.inputStream = inputStream;
        this.filename = filename==null?"":filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFilename() {
        return filename;
    }

    /**
     * 方法名: SmartInputStrem.createDownLoad
     * 作者: LSL
     * 创建时间: 17:26 2018\6\29 0029
     * 描述: 跟据输入流创建文件下载
     * 参数: [in, filename]
     * 返回: com.lsl.smartweb.view.SmartInputStrem
     */
    public static SmartInputStrem createFileDownLoad(InputStream in,String filename){
        return new SmartInputStrem(in,filename);
    }

}
