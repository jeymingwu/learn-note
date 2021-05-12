package com.example.aop.aspectj.pointcut;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 定义切点名称
 * @author : jeymingwu
 * @date : 2021-05-12
 **/

public class TestNamePointcut {

    // 通过注解方法 inPackage() 对该切点进行命名，方法可视修饰符 private —— 该切点只能在本切点类中使用
    @Pointcut("within(com.example.aop.advice.*)")
    private void inPackage(){

    }

    // 通过注解方法 greetTo() 对该切点进行命名，方法可视修饰符 protected —— 该切点只能在当前包中的切点类、子切面类中使用
    @Pointcut("execution(* greetTo(..))")
    protected void greetTo() {

    }

    // 通过注解方法 inPkgGreetTo() 对该切点进行命名，本切点也是命名切点，方法可视修饰符 public
    @Pointcut("inPackage() &amp;&amp; greetTo()")
    public void inPkgGreetTo() {

    }
}
