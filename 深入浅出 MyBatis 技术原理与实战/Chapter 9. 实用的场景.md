# 实用场景

## 9.1 数据库 BLOB 字段的读取

+ BLOB ：大数据段、常用于文件操作（ps：书上写）【正确做法：存储文件的路径】
+ 若文件内容过大，易造成内存溢出；
+ MyBatis -> typeHandler 提供支持：
    + BlobTypeHandler：常用
    + BlobByteObjectArrayTypeHandler：用于数据库的兼容性

```sql
CREATE TABLE t_file (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    file BLOB
);
```

```java
// pojo
import lombok.AllArgsConstructor;
import lombok.Builder;import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TFile {
    private Long id;
    private byte[] file;
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mapper.FileMapper">
    <insert id="insertFile" keyProperty="id" useGeneratedKeys="true" parameterType="com.example.pojo.TFile">
        INSERT INTO t_file(file) VALUES (#{file})
    </insert>
    <select id="getFile" parameterType="int" resultType="com.example.pojo.TFile">
        SELECT id, file FROM t_file
    </select>
</mapper>
```

## 9.2 批量更新

+ 使用批量更新有助于提高性能；
+ 启用配置：
    + XML 配置文件：<settings><setting name="defaultExecutorType" value="BATCH"/></settings>
    + JavaBean Config：SqlSessionFactory.openSession(ExecutorType.BATCH);
+ 注意：一旦启用了批量执行器，默认情况下需 commit 后才提交 SQL 语句；
+ 若在同一个事务中因后续任有其他的语句要执行而并不想提交事务，可执行 SqlSession 中的 flushStatements 方法即可；目的是将当前缓存 SQL 发送给数据库执行； 

## 9.3 调用存储过程

## 9.4 分表

## 9.5 分页

## 9.6 上传文件到服务器

## 9.7 在映射中使用枚举

## 9.8 多对多级联

## 9.9 总结