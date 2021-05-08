package com.example.aop.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class GreetingBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        String clientName = (String) objects[0];
        System.out.println("How are you! Mr." + clientName + ".");
    }
}
