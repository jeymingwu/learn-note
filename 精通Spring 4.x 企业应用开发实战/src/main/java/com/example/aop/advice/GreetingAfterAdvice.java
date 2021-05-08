package com.example.aop.advice;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author : jeymingwu
 * @date : 2021-05-07
 **/

public class GreetingAfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("Please enjoy yourself!");
    }
}
