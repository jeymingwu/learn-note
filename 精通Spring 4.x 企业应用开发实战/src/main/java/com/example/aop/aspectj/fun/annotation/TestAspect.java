package com.example.aop.aspectj.fun.annotation;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author : jeymingwu
 * @date : 2021-05-11
 **/
@Aspect
public class TestAspect {

    @AfterReturning("@annotation(com.example.aop.aspectj.annotation.NeedTest)")
    public void needTestFun() {
        System.out.println("needTestFun() executed!");
    }
}
