#  MyBatis 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- MyBatis 配置 XML 文件的层次结构 -->
<configuration>
    <properties/> <!-- 配置 -->
    <settings/> <!-- 属性  -->
    <typeAliases/><!-- 设置 -->
    <typeHandlers/><!-- 类型命名 -->
    <objectFactory/><!-- 对象工厂 -->
    <plugins/><!-- 插件 -->
    <environments><!-- 配置环境 -->
        <environment> <!-- 配置变量 -->
            <transactionManager/><!-- 事务管理器 -->
            <dataSource/><!-- 数据源 -->
        </environment>
    </environments>
    <databaseIdProvider/> <!-- 数据库厂商标识 -->
    <mappers/><!-- 映射器 -->
</configuration>
```

## 3.1 properties 元素
+  配置属性的元素，在配置文件的上下文中使用；
+  三种配置方式：
    +  property 子元素
    +  properties 配置文件
    +  程序参数传递
+  优先级（由上到下，优先级逐渐变高）：
    +  在 properties 元素体内指定的属性**首先被读取**；
    +  根据 properties 元素中的 resource 属性读取类路径下属性文件，或者根据 url 属性指定的路径读取属性文件，并覆盖已读取的同名属性；
    +  读取作为方法参数传递的属性，并覆盖已读取的同名属性；
+  注意：
    +  不要混合方式使用；
    +  首选 properties；

+ property 子元素
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <propertires>
        <property name="username" value="root"></property>
        <property name="password" value="123456"></property>
        <property name="password2" value="${password}"></property> <!-- 引用 -->
    </propertires>
</configuration>
```

+ properties 配置文件
```properties
username=root
password=123456
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <propertires resource="jdbc.properties" />
</configuration>
```

+  程序参数传递（例子：从文件中读取加密的配置信息，经过解密后，再创建 SqlSessionFactory）
```java
import org.apache.ibatis.io.Resources;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.io.Reader;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;
public class Demo {
    private InputStream cfgStream = null;
    private Reader cfgReader = null;
    private InputStream proStream = null;
    private Reader proReader = null;
    private Properties properties = null;

    private SqlSessionFactory sqlSessionFactory = null;    

    public void action() {
        try {
            // 读入配置文件流
            cfgStream = Resources.getResourceAsStream("mybatis-config.xml");
            cfgReader = new InputStreamReader(cfgStream);
            
            // 读入属性文件
            proStream = Resources.getResourceAsStream("jdbc.properties");
            proReader = new InputStreamReader(proStream);
            
            properties = new Properties();
            properties.load(proReader);
            
            // 解密为明文示例
            properties.setProperty("username", decode(properties.getProperty("username")));
            properties.setProperty("password", decode(properties.getProperty("password")));
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    
        synchronized (this.getClass()) {
            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(cfgStream, properties);
            }
        }
    }
}
```

## 3.2 [设置](https://mybatis.org/mybatis-3/zh/configuration.html#settings)

## 3.3 别名

## 3.4 typeHandler 类型处理器

## 3.5 ObjectFactory

## 3.6 插件

## 3.7 environments 配置环境

## 3.8 databaseIdProvider 数据库厂商标识

## 3.9 引入映射器的方法