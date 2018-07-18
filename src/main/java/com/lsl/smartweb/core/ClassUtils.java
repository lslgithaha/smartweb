package com.lsl.smartweb.core;

import com.lsl.smartweb.configure.SmartConfig;
import com.lsl.smartweb.utils.StringUtils;
import com.lsl.smartweb.view.ReturnUtls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {
    private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);
    private static final String FILE = "file";
    private static final String JAR = "jar";
    /**
     * 方法名: ClassUtils.getClassLoader
     * 作者: LSL
     * 创建时间: 16:26 2018\5\8 0008
     * 描述: 获得类加载器
     * 参数: []
     * 返回: java.lang.ClassLoader
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
    /**
     * 方法名: ClassUtils.loadClass
     * 作者: LSL
     * 创建时间: 16:28 2018\5\8 0008
     * 描述: 加载类
     * 参数: [className, inInitialized]
     * 返回: java.lang.Class<?>
     */
    public static Class<?> loadClass(String className,boolean inInitialized){
        Class<?> cls;
        try {
            cls = Class.forName(className,inInitialized,getClassLoader());
            log.debug("load class success:"+className);
        } catch (ClassNotFoundException e) {
            log.error("load class false,msg:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return cls;
    }
    /**
     * 方法名: ClassUtils.getClassSet
     * 作者: LSL
     * 创建时间: 16:36 2018\5\8 0008
     * 描述: 获得指定包下所有类
     * 参数: [packageName]
     * 返回: java.util.Set<java.lang.Class<?>>
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> clsSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources("");//获得classpath资源路径
//            Enumeration<URL> urls = getClassLoader().getResources("");//获得classpath资源路径
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    String protocol = url.getProtocol();
                    if(protocol.equals(FILE)){
//                        String path = url.getPath().replaceAll("%20", " ");
                        String path = url.getPath().replaceAll("%20", " ")+packageName.replace(".","/");
                        addClass(clsSet,path,packageName);
                    }else if(protocol.equals(JAR)){
                        dealJarReaource(url,clsSet);
                    }
                }
            }
            //兼容tomcat7及其以下
            if(!clsSet.contains(loadClass("com.lsl.smartweb.core.BeanHelper",false))){
                log.debug("检测到使用tomcat8以下的服务器容器！");
                String path = new File(ClassUtils.class.getClassLoader().getResource("").getPath()).getParent()+File.separator+"lib";
                File[] list = new File(path).listFiles(file -> file.getName().startsWith("smartweb"));
                for (File file : list) {
                    String s = "jar: file:/" + file.getAbsolutePath().replaceAll("\\\\","/")+"!/";
                    ClassUtils.dealJarReaource(new URL(s),clsSet);
                }
            }
        } catch (Exception e) {
            log.error("load class false,msg:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return clsSet;
    }

    /**
     * 方法名: ClassUtils.dealJarReaource
     * 作者: LSL
     * 创建时间: 14:07 2018\7\18 0018
     * 描述:
     * 参数: [url, clsSet]
     * 返回: void
     */
    protected static void dealJarReaource(URL url, Set<Class<?>> clsSet) throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        if(jarURLConnection != null){
            JarFile jarFile = jarURLConnection.getJarFile();
            if(jarFile != null){
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()){
                    JarEntry jarEntry = entries.nextElement();
                    String name = jarEntry.getName();
                    if(name.endsWith(".class")){
                        String s = name.substring(0, name.lastIndexOf(".")).replaceAll("/", ".");
                        if(SmartConfig.getJatInitBase(s))
                            clsSet.add(loadClass(s,false));
                    }else if(name.endsWith("FileContentType.txt")){
                        InputStream inputStream = jarFile.getInputStream(jarEntry);
                        ReturnUtls.init(inputStream);
                    }
                }
            }
        }
    }

    /**
     * 方法名: ClassUtils.addClass
     * 作者: LSL
     * 创建时间: 11:48 2018\5\9 0009
     * 描述: 将类添加的指定set集合
     * 参数: [clsSet, path, packageName]
     * 返回: void
     */
    public static void addClass(Set<Class<?>> clsSet, String path, final String packageName){
        final File[] files = new File(path).listFiles(pathname -> (pathname.isFile() && pathname.getName().endsWith(".class"))||pathname.isDirectory());
        for (File file:files) {
            if (file.isFile()) {
                String filename = file.getName();
                String className = filename.substring(0, filename.lastIndexOf("."));
                if(StringUtils.isNotEmpty(packageName)){
                    className = packageName+ "." +className;
                }
                clsSet.add(loadClass(className,false));
            }else{
                String packagePath = file.getName();
                if(StringUtils.isNotEmpty(path)){
                    packagePath = path +"/"+ packagePath;
                }
                String packsgeName = file.getName();
                if(StringUtils.isNotEmpty(packageName)){
                    packsgeName = packageName + "." + packsgeName;
                }
                addClass(clsSet,packagePath,packsgeName);
            }
        }
    }

}
