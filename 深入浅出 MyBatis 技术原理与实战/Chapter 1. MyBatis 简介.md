#  MyBatis 简介

## 传统 JDBC 编程
[传统 JDBC 编程 demo](../src/main/java/mybatis/jdbc/JdbcExample.java)

大致步骤：
+  连接数据库，注册驱动和数据库信息；
+  操作 Connection，打开 Statement 对象；
+  通过 Statement 执行 SQL，返回结果到 ResultSet 对象；
+  使用 ResultSet 读取数据，通过代码转化为具体的 POJO 对象；
+  关闭相关资源；

弊端：
+  工作量大；
+  需捕捉产生的异常并正确关闭资源；

## ORM 模型

ORM ：Object Relational Mapping；数据库的表和简单 Java 对象 POJO  的映射关系模型；
POJO ： Plain Ordinary Java Project

## Hibernate

缺点：
+  全表映射带来不便，比如更新时需要发送所有字段；
+  无法根据不同的条件组装不同的 SQL；
+  对多表关联和复杂的 SQL 查询支持较差，需手写 SQL，返回后需手动封装 POJO；
+  不能有效支持存储过程；
+  HQL 性能较差；需要优化 SQL；

## MyBatis
+  半自动映射框架；手工匹配提供 POJO 、 SQL 和映射关系；
+  MyBatis 映射文件包含：
    +  SQL
    +  映射规则
    +  POJO