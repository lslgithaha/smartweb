package com.lsl.smartweb.configure;

import com.lsl.smartweb.utils.ArrayUtils;

/**
 * Create by LSL on 2018\6\20 0020
 * 描述：
 * 版本：1.0.0
 */
public class SmartConfig {
    private static String[] resource;
    private static String[] jatInitBase;
    private static String exception;
    private static String interfaceNotFond;
    private static String upload_tempdir;
    private static int upload_filemaxsize;
    private static int upload_maxsize;
    private static int upload_usecachesize;
    private static String progress;
    private static String transaction;

    public static String getTransaction() {
        return transaction;
    }

    public static void setTransaction(String transaction) {
        SmartConfig.transaction = transaction;
    }

    public static void setResource(String[] resource) {
        SmartConfig.resource = resource;
    }

    public static boolean getJatInitBase(String s) {
        if (s.startsWith("com.lsl.smartweb")) {
            return true;
        }
        if (ArrayUtils.isNotEmpty(jatInitBase)) {
            for (String name : jatInitBase) {
                if (s.startsWith(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setJatInitBase(String[] jatInitBase) {
        SmartConfig.jatInitBase = jatInitBase;
    }

    public static String getProgress() {
        return progress;
    }

    public static void setProgress(String progress) {
        SmartConfig.progress = progress;
    }

    public static String getUpload_tempdir() {
        return upload_tempdir;
    }

    public static void setUpload_tempdir(String upload_tempdir) {
        SmartConfig.upload_tempdir = upload_tempdir;
    }

    public static int getUpload_filemaxsize() {
        return upload_filemaxsize;
    }

    public static void setUpload_filemaxsize(int upload_filemaxsize) {
        SmartConfig.upload_filemaxsize = upload_filemaxsize;
    }

    public static int getUpload_maxsize() {
        return upload_maxsize;
    }

    public static void setUpload_maxsize(int upload_maxsize) {
        SmartConfig.upload_maxsize = upload_maxsize;
    }

    public static int getUpload_usecachesize() {
        return upload_usecachesize;
    }

    public static void setUpload_usecachesize(int upload_usecachesize) {
        SmartConfig.upload_usecachesize = upload_usecachesize;
    }


    public static String getInterfaceNotFond() {
        return interfaceNotFond;
    }

    public static void setInterfaceNotFond(String interfaceNotFond) {
        SmartConfig.interfaceNotFond = interfaceNotFond;
    }


    public static String getException() {
        return exception;
    }

    public static void setException(String exception) {
        SmartConfig.exception = exception;
    }


    public static String[] getResource() {
        return resource;
    }

    public static void setResouce(String[] resource) {
        SmartConfig.resource = resource;
    }

}
