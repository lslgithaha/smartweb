package com.lsl.smartweb.core;

import com.lsl.smartweb.fileup.SmartFile;

import java.util.Map;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：参数封装
 * 版本：1.0.0
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }
    public long getLong(String name){
        return Long.valueOf(paramMap.get(name).toString());
    }
    public int getInt(String name){
        Object o = paramMap.get(name);
        if(o==null){
            return 0;
        }
        return Integer.valueOf((String)o);
    }
    public char getChar(String name){
        Object o = paramMap.get(name);
        if(o==null){
            return ' ';
        }
        return ((String)o).toCharArray()[0];
    }
    public boolean getBoolean(String name){
        Object o = paramMap.get(name);
        if(o==null){
            return false;
        }
        return Boolean.valueOf((String)o);
    }
    public float getFloat(String name){
        return Float.valueOf(paramMap.get(name).toString());
    }
    public double getDouble(String name){
        Object o = paramMap.get(name);
        if(o==null){
            return 0.0d;
        }
        return Double.valueOf((String)o);
    }
    public String getString(String name){
        Object o = paramMap.get(name);
        if(o == null){
            return null;
        }
        return (String)o;
    }
    public byte getByte(String name){
        Object o = paramMap.get(name);
        if(o==null){
            return (byte)0;
        }
        return Byte.valueOf((String)o);
    }
    public short getShort(String name){
        Object o = paramMap.get(name);
        if(o==null){
            return (short) 0;
        }
        return Short.valueOf((String)o);
    }

    public SmartFile getSmarFile(String s) {
        Object o = paramMap.get(s);
        if(null == o){
            return null;
        }
        return (SmartFile) o;
    }
}
