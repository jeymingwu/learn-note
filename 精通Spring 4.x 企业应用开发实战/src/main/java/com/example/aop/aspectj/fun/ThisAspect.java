package com.example.aop.aspectj.fun;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author : jeymingwu
 * @date : 2021-05-12
 **/
@Aspect
public class ThisAspect {

    @AfterReturning("this(com.example.aop.aspectj.Seller)")
    public void thisTest() {
        System.out.println("thisTest() executed!");
    }

}
