package com.example.aspectj.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 1.声明注解的保留期限
@Retention(RetentionPolicy.RUNTIME)
// 2.声明可以使用该注解的目标类型
@Target(ElementType.METHOD)
// 3.定义注解
public @interface NeedTest {

    // 4.声明注解成员
    boolean value() default true;
}
