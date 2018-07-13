package com.lsl.smartweb.configure;

import com.lsl.smartweb.db.pool.ConectFactory;
import com.lsl.smartweb.db.pool.PoolManage;
import com.lsl.smartweb.utils.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by LSL on 2018\6\19 0019
 * 描述：配置文件解析
 * 版本：1.0.0
 */
public class ReadXml {
    private static final Logger log = LoggerFactory.getLogger(ReadXml.class);

    /**
     * 方法名: ReadXml.read
     * 作者: LSL
     * 创建时间: 15:32 2018\6\20 0020
     * 描述: 读取smart.xml
     * 参数: [path]
     * 返回: org.jdom.Element
     */
    public static void read(String path) {
        try {
            path += File.separator + "smart.xml";
            log.info("smartweb配置文件：{}", path);
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new FileInputStream(path));
            Element rootElement = document.getRootElement();
            resourceInit(rootElement);
            exceptionInit(rootElement);
            databaseInit(rootElement);
            interfaceNotFoundInit(rootElement);
            uploadInit(rootElement);
            jarNeedLoadBasePacsge(rootElement);
            transactionInit(rootElement);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法名: ReadXml.resourceInit
     * 作者: LSL
     * 创建时间: 15:34 2018\6\20 0020
     * 描述: 注册静态资源
     * 参数: []
     * 返回: void
     */
    private static void resourceInit(Element rootElement) {
        String resource = rootElement.getChild("resource").getText();
        String[] split = resource.split(",");
        SmartConfig.setResouce(split);
    }

    private static void exceptionInit(Element rootElement) {
        String resource = rootElement.getChild("exception").getText().trim();
        SmartConfig.setException(resource);
    }

    /**
     * 方法名: ReadXml.databaseInit
     * 作者: LSL
     * 创建时间: 10:42 2018\6\25 0025
     * 描述: m默认的连接池初始化
     * 参数: [rootElement]
     * 返回: void
     */
    private static void databaseInit(Element rootElement) {
        Element database = rootElement.getChild("database");
        if (database == null) {
            log.debug("没有配置默认db");
            return;
        }
        String useful = database.getChild("useful").getText();
        boolean use = Boolean.valueOf(useful);
        PoolManage.setInit(use);
        if(use){
            String diver = database.getChild("diver").getText();
            String url = database.getChild("url").getText();
            String user = database.getChild("user").getText().trim();
            String password = database.getChild("password").getText().trim();
            ConectFactory.setDriver(diver);
            ConectFactory.setUrl(url);
            ConectFactory.setUser(user);
            ConectFactory.setPassword(password);
            log.info("数据库连接池相关信息：url:{},user:{},diver:{},password:{}", url, user, diver, password);
            String refreshtime = database.getChild("refreshtime").getText().trim();
            int time = formatIn(refreshtime);
            PoolManage.setRefreshTime(time * 1000);
            String maxConnection = database.getChild("maxConnection").getText().trim();
            PoolManage.setMaxConnect(Integer.valueOf(maxConnection));
            String minConnection = database.getChild("minConnection").getText().trim();
            PoolManage.setMinConnect(Integer.valueOf(minConnection));
            String overtime = database.getChild("overtime").getText().trim();
            PoolManage.setOvertime(Integer.valueOf(overtime));
            String refreshsql = database.getChild("refreshsql").getText();
            PoolManage.setRefreshsql(refreshsql);
            log.info("连接池相关信息->刷新时间（秒）：{},最大连接数：{},最小连接数：{},连接超时（秒）:{},刷新连接用的sql：{}", time, maxConnection, minConnection, overtime, refreshsql);
        }
    }

    /**
     * 方法名: ReadXml.interfaceNotFoundInit
     * 作者: LSL
     * 创建时间: 10:00 2018\6\27 0027
     * 描述: 接口未找到提示信息
     * 参数: [rootElement]
     * 返回: void
     */
    public static void interfaceNotFoundInit(Element rootElement) {
        String InterfaceNotFound = rootElement.getChild("InterfaceNotFound").getText().trim();
        SmartConfig.setInterfaceNotFond(InterfaceNotFound);
        log.debug("接口找不到返回信息：{}", InterfaceNotFound);
    }

    /**
     * 方法名: ReadXml.uploadInit
     * 作者: LSL
     * 创建时间: 10:03 2018\6\27 0027
     * 描述: 文件上传相关配置初始化
     * 参数: [rootElement]
     * 返回: void
     */
    private static void uploadInit(Element rootElement) {
        Element file_upload = rootElement.getChild("file_upload");
        int usecachesize = formatIn(file_upload.getChild("usecachesize").getText().trim());
        int filemaxsize = formatIn(file_upload.getChild("filemaxsize").getText().trim());
        int maxsize = formatIn(file_upload.getChild("maxsize").getText().trim());
        SmartConfig.setUpload_filemaxsize(filemaxsize);
        SmartConfig.setUpload_maxsize(maxsize);
        SmartConfig.setUpload_usecachesize(usecachesize);
        String tmpdir = file_upload.getChild("tmpdir") == null ? "" : file_upload.getChild("tmpdir").getText();
        if (StringUtils.isNotEmpty(tmpdir)) {
            SmartConfig.setUpload_tempdir(tmpdir.trim());
        }
        String progress = file_upload.getChild("progress") == null ? "fileupload_process" : file_upload.getChild("progress").getText();
        SmartConfig.setProgress(progress);
        log.debug("file_upload_int:\\{useCacheSize:{},fileMaxSize:{},maxSize:{},tmpDir:{}\\}", usecachesize, filemaxsize, maxsize, tmpdir);
    }

    private static int formatIn(String str) {
        int val = 1;
        String[] split = str.split("\\*");
        for (String s : split) {
            val *= Integer.valueOf(s);
        }
        return val;
    }

    /**
     * 方法名: ReadXml.uploadInit
     * 作者: LSL
     * 创建时间: 10:03 2018\6\27 0027
     * 描述: 若带有samart注解的类打包jar，需在此添加路径
     * 参数: [rootElement]
     * 返回: void
     */
    private static void jarNeedLoadBasePacsge(Element rootElement) {
        String jarInitBase = rootElement.getChild("jarInitBase").getText().trim();
        if (StringUtils.isNotEmpty(jarInitBase)) {
            SmartConfig.setJatInitBase(jarInitBase.split(","));
        }
    }
    /**
     * 方法名: ReadXml.uploadInit
     * 作者: LSL
     * 创建时间: 10:03 2018\6\27 0027
     * 描述: 声明式事务正则
     * 参数: [rootElement]
     * 返回: void
     */
    private static void transactionInit(Element rootElement) {
        Element transaction = rootElement.getChild("transaction");
        if(transaction == null){
            SmartConfig.setTransaction("ee12b625ec0e41489dd66ae5b9c67366");
            return;
        }
        String text = transaction.getText();
        if(!StringUtils.isNotEmpty(text)){
            SmartConfig.setTransaction("ee12b625ec0e41489dd66ae5b9c67366");
            return;
        }
        SmartConfig.setTransaction(text);
    }
}
