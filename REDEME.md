##smartweb使用说明
####1.配置说明
~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<smart>
    <!-- 静态资源注册（常见静态资源已经默认注册，若发现静态资源被拦截则在此配置,多个用“,”隔开），示例如下 -->
    <resource>*.pdf,*.pad</resource>
    <!--统一异常处理类，按需写代码，需继承com.lsl.smartweb.aop.ExceptionHandler-->
    <exception></exception>
    <!--申明式事务配置 方法名匹配正则表达式-->
    <transaction>^[\w]*Save$</transaction>
    <!-- 连接池配置,自写持久化层可以使用com.lsl.smartweb.db.DbManage.getConn()从连接池获取连接 -->
    <database>
        <!--是否启用数据库连接池-->
        <useful>true</useful>
        <!--jdbc驱动-->
        <diver>com.mysql.cj.jdbc.Driver</diver>
        <!-- 连接字符串 -->
        <url>jdbc:mysql://10.100.100.6:3306/test?useUnicode=true&amp;characterEncoding=UTF-8</url>
        <!--用户名-->
        <user>root</user>
        <!--密码-->
        <password>root</password>
        <!--连接刷新时间(秒)-->
        <refreshtime>5*60</refreshtime>
        <!--最大连接数-->
        <maxConnection>10</maxConnection>
        <!--常驻最小连接数-->
        <minConnection>2</minConnection>
        <!-- 等待超时时间（秒） -->
        <overtime>3</overtime>
        <!-- smartweb连接池刷新连接选择了执行简单sql，减小连接断开然后重新建立的系统资源开销 -->
        <refreshsql>select version()</refreshsql>
    </database>
    <!--文件上传相关配置-->
    <file_upload>
        <!--文件大小超过多少开始使用磁盘缓存(b)-->
        <usecachesize>1024*1024</usecachesize>
        <!--单个文件最大体积（b）-->
        <filemaxsize>-1</filemaxsize>
        <!--每次上传文件总大小 -1无上限 -->
        <maxsize>-1</maxsize>
        <!--缓存目录（若不设置，将使用默认目录），
        使用SmartFile的write方法会自动缓存文件会删除，若是调用getInputStream方法自己操作流则还需自己调用deleteTemp方法删除临时文件-->
        <tmpdir>E:</tmpdir>
        <!--文件上传进度相关的信息（百分比，速度，耗时等等的访问路径）-->
        <progress>/progress</progress>
    </file_upload>
    <!--若带有smart注解的类被打成jar包，则需要在此添加base路径,多个用“,”隔开-->
    <!--如果打包成jar的类全路径为com.hah.test.Jarclass,以下4种形式都可以，但是建议越全越好 -->
    <!--<jarInitBase>com, com.hah, com.hah.test, com.hah.test.Jarclass</jarInitBase>-->
    <jarInitBase></jarInitBase>
</smart>
~~~
####注解说明
	1.Bean：在类上标识当前类需要初始化到smatweb容器
	2.Inter：在成员变量上标识需要注入
	3.Controller("/路径1"):mvc开发中Controller层标志
	4.GET("/路径2"):标志方法上标识访问接口路径（/路径1/路径2），接受get请求
	5.POST("/路径3")：标志方法上标识访问接口路径（/路径1/路径3），接受post请求
	6.Service:mvc开发中Service层标志
	7.Dao:mvc开发中Dao层标志
	8.Transaction:用于Service的方法，该方法名不在申明式事务配置的规则中可强制开启事务
	9.Aspect：用于切面代理，例如：@Aspect(Controller.class)表示未所有的Controller生成代理

####开发说明
######1.开发统一异常处理类
	实现接口ExceptionHandler，resolveException_Json（）处理json类请求异常，resolveException_View处理页面跳转的求情异常,处理接口未找到resolveException_Method_not_define()
######2.数据库操作
	smartweb已实现简单的数据库执行详见SqlTemple类
######3.开发切面代理类
   为Controller切面代理为例，类首先继承AspectProxy，标上注解@Aspect(Controller.class),代码如下
~~~java
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    private static final Logger log = LoggerFactory.getLogger(ControllerAspect.class);
    @Override
    public void begin() {//一开始执行的方法
    }
    @Override
    public void end() {//最后执行的方法
    }
    public boolean needIntercept(Class<?> cls, Method method, Object[] params) throws Throwable {//判断是否需要执行代理方法
        return super.needIntercept(cls, method, params);
    }
    private long begin;
    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {//判断需要执行后在方法前执行前执行
        log.debug("class:{} , method:{} ",cls.getName(),method.getName());
        begin = System.currentTimeMillis();
    }
    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {//判断需要执行后在方法执行后执行
        log.debug("time:{}",System.currentTimeMillis()-begin);
    }
}
~~~
######4.日志
	smartweb默认使用sl4j的logback实现，需要其他自行配置
######5.Controller层特殊返回值处理
	1.json：直接返回实体类即可
	2.文件：①文件已知路径（E:\\1.xls），return "file:E:\\1.xls"即可，需要指定下载的文件名时使用return "file:E:\\1.xls,filename=文件名";②文件是流，返回SmartInputStrem.createFileDownLoad(InputStream in,String filename)即可
	3.图片(E:\\ss.png)，return "img:E:\\ss.png",指定图片尺寸：return "img:E:\\ss.png,size=800x700"，指定尺寸和文件名return "img:E:\\ss.png,size=800x700,filename=1.png"
	4.请求转发:return "forward:请求路径"
	5.重定向：return "redirect:重定向路径"
	6.返回文本：return “文本内容”
