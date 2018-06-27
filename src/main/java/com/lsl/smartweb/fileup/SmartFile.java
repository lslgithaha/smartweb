package com.lsl.smartweb.fileup;

import org.apache.commons.fileupload.FileItem;

import java.io.*;

/**
 * Create by LSL on 2018\6\26 0026
 * 描述：
 * 版本：1.0.0
 */
public class SmartFile {
    /**
     * 文件流
     */
    private InputStream inputStream;
    /**
     * 输入框name属性
     */
    private String name;
    /**
     * 文件名
     */
    private String fileName;


    /**
     * 文件名
     */
    private String contentType;

    /**
     * 文件流大小
     */
    private long size;

    private File temp;


    public SmartFile(InputStream inputStream, String name, String fileName, String contentType, long size,File temp) {
        this.inputStream = inputStream;
        this.name = name;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.temp = temp;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public void write(String filepath) {
        long l = System.currentTimeMillis();
        try {
            FileOutputStream out = new FileOutputStream(filepath);
            byte[] b;
            if (size > 1024 * 1024 * 100) {
                b = new byte[1024 * 1024];
            } else {
                b = new byte[1024];
            }
            int n = 0;
            while ((n = inputStream.read(b)) != -1) {
                out.write(b, 0, n);
            }
            inputStream.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            deleteTemp();
        }
    }
    @Override
    protected void finalize() throws Throwable {
        deleteTemp();
    }
    public void deleteTemp(){
        this.temp.delete();
    }
}
