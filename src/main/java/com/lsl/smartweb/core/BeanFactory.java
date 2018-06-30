package com.lsl.smartweb.core;

import com.lsl.smartweb.fileup.SmartFile;
import com.lsl.smartweb.utils.Util;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Create by LSL on 2018\5\8 0008
 * 描述：豆子小作坊
 * 版本：1.0.0
 */
public final class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    /**
     * 方法名: BeanFactory.newInstance
     * 作者: LSL
     * 创建时间: 18:11 2018\5\8 0008
     * 描述: 实例化类
     * 参数: [cls]
     * 返回: java.lang.Object
     */
    public static Object newInstance(Class<?> cls){
        Object o;
        try {
            o = cls.newInstance();
            log.debug("newInstance "+cls.getName()+" success");
        } catch (Exception e) {
            log.error("newInstance "+cls.getName()+" error,msg:"+e.getMessage());
            throw  new RuntimeException(e);
       }
       return o;
    }
    /**
     * 方法名: BeanFactory.getMethodInfo
     * 作者: LSL
     * 创建时间: 15:16 2018\5\11 0011
     * 描述: 获得参数类型和名字的对应关系
     * 参数: [method]
     * 返回: java.util.Map<java.lang.Class<?>,java.lang.String>
     */
    public static String[] getMethodInfo(Method method){
        try {
            Class<?> clazz = method.getDeclaringClass();
            ClassPool pool = ClassPool.getDefault();
            pool.appendClassPath(new ClassClassPath(clazz));
            CtClass clz = pool.get(clazz.getName());
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;
            CtClass[] params = new CtClass[length];
            for (int i = 0; i < length; i++) {
                params[i] = pool.getCtClass(method.getParameterTypes()[i].getName());
            }
            CtMethod cm = clz.getDeclaredMethod(method.getName(), params);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                    .getAttribute(LocalVariableAttribute.tag);
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            String[] paramNames = new String[parameterTypes.length];
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = attr.variableName(i + pos);
            }
            return paramNames;
    } catch (NotFoundException e) {
            log.error("ClassPool load  "+method.getName()+" error,msg:"+e.getMessage());
            throw  new RuntimeException(e);
        }
    }
    /**
     * 方法名: BeanFactory.dualMethod
     * 作者: LSL
     * 创建时间: 10:36 2018\5\11 0011
     * 描述: 参数装填
     * 参数: [method, val, param]
     * 返回: java.util.List<java.lang.Object>
     */
    public static List<Object> dualMethod(Method method, HashMap<String, Object> val, Param param){
        List<Object> parm = new ArrayList<Object>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] methodInfo = getMethodInfo(method);
        if(parameterTypes ==null || parameterTypes.length==0){
           return null;
        }
        int index=0;
        for (Class<?> type : parameterTypes) {
            if(type.isArray()){
                String[] values = ((HttpServletRequest) val.get("request")).getParameterValues(methodInfo[index]);
                parm.add(values);
            }else if (type.equals(ServletContext.class)) {
                parm.add(val.get("application"));
            } else if (type.equals(HttpSession.class)) {
                parm.add(val.get("session"));
            } else if (type.equals(HttpServletRequest.class)) {
                parm.add(val.get("request"));
            } else if (type.equals(HttpServletResponse.class)) {
                parm.add(val.get("response"));
            } else if (type.equals(ServletResponse.class)) {
                parm.add(val.get("res"));
            } else if (type.equals(ServletRequest.class)) {
                parm.add(val.get("req"));
            }else if(type.equals(SmartFile.class)){
                parm.add(param.getSmarFile(methodInfo[index]));
            }else if(type.equals(int.class) || type.equals(Integer.class)){
                parm.add(param.getInt(methodInfo[index]));
            }else if(type.equals(byte.class) || type.equals(Byte.class)){
                parm.add(param.getByte(methodInfo[index]));
            }else if(type.equals(short.class) || type.equals(Short.class)){
                parm.add(param.getShort(methodInfo[index]));
            }else if(type.equals(char.class) || type.equals(Character.class)){
                parm.add(param.getChar(methodInfo[index]));
            }else if(type.equals(long.class) || type.equals(Long.class)){
                parm.add(param.getLong(methodInfo[index]));
            }else if(type.equals(double.class) || type.equals(Double.class)){
                parm.add(param.getDouble(methodInfo[index]));
            }else if(type.equals(float.class) || type.equals(Float.class)){
                parm.add(param.getFloat(methodInfo[index]));
            }else if(type.equals(boolean.class) || type.equals(Boolean.class)){
                parm.add(param.getBoolean(methodInfo[index]));
            }else if(type.equals(String.class)){
                parm.add(param.getString(methodInfo[index]));
            }else{
                parm.add(Util.toBean(param.getParamMap(),type));
            }
            index++;
        }

        return parm;
    }
    /**
     * 方法名: BeanFactory.invokeMethod
     * 作者: LSL
     * 创建时间: 9:02 2018\5\9 0009
     * 描述: 调用返回结果方法
     * 参数: [o, method, args]
     * 返回: java.lang.Object
     */
    public static Object invokeMethod(Object o, Method method, HashMap<String, Object> val, Param param) throws Exception {
        Object result;
        method.setAccessible(true);
        try {
            List<Object> list = dualMethod(method, val, param);
            if(list == null){
                result = method.invoke(o);
            }else{
                result = method.invoke(o, list.toArray());
            }
        } catch (Exception e) {
            log.error("invoke method {} false,msg:",method.getName(),e);
            throw e;
        }
        return result;
    }
    /**
     * 方法名: BeanFactory.setFiled
     * 作者: LSL
     * 创建时间: 9:05 2018\5\9 0009
     * 描述: 设置成员变量
     * 参数: [o, field, v]
     * 返回: void
     */
    public static void setFiled(Object o, Field field,Object v){
        try {
            field.setAccessible(true);
            field.set(o,v);
        } catch (IllegalAccessException e) {
            log.error("set Filed {} false,msg",field.getName(),e);
            throw  new RuntimeException(e);
        }
    }
}
