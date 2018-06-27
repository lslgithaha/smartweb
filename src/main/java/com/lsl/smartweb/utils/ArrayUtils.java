package com.lsl.smartweb.utils;

/**
 * Create by LSL on 2018\6\20 0020
 * 描述：
 * 版本：1.0.0
 */
public class ArrayUtils {
    public static boolean isEmpty(String[] arr){
        return arr==null||arr.length==0;
    }
    public static boolean isNotEmpty(String[] arr){
        return !isEmpty(arr);
    }
}
