package com.lsl.smartweb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：流操作类
 * 版本：1.0.0
 */
public final class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);
    /**
     * 方法名: Util.getString
     * 作者: LSL
     * 创建时间: 13:48 2018\5\9 0009
     * 描述: 从流中获取字符串
     * 参数: [is]
     * 返回: java.lang.String
     */
    public static String getString(InputStream is){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bf.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("从输入流中获取字符串失败,错误信息:"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法名: Util.encodeURL
     * 作者: LSL
     * 创建时间: 13:52 2018\5\9 0009
     * 描述: 编码
     * 参数: [source]
     * 返回: java.lang.String
     */
    public static String encodeURL(String source){
        String target;
        try {
            target = URLEncoder.encode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("编码失败,错误信息:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return target;
    }
    /**
     * 方法名: Util.decodeURL
     * 作者: LSL
     * 创建时间: 13:52 2018\5\9 0009
     * 描述: 解码
     * 参数: [source]
     * 返回: java.lang.String
     */
    public static String decodeURL(String source){
        String target;
        try {
            target = URLDecoder.decode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("编码失败,错误信息:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return target;
    }
    public static <T> String toJson(T o){
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("解析为json失败,,错误信息:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return json;
    }
    public static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try {
            pojo = new ObjectMapper().readValue(json,type);
        } catch (Exception e) {
            log.error("解析为json失败,,错误信息:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return pojo;
    }
    public static <T> T toBean(Map<String, Object> hashMap, Class<T> type){
        T pojo;
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Field[] fields = type.getDeclaredFields();
            if(fields !=null && fields.length>0){
                for (Field field : fields) {
                    map.put(field.getName(),hashMap.get(field.getName()));
                }
            }
            pojo = fromJson(toJson(map),type);
        } catch (Exception e) {
            log.error("解析为json失败,,错误信息:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return pojo;
    }

}
