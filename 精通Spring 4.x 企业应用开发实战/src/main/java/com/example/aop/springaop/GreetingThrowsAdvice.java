package com.example.aop.springaop;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * @author : jeymingwu
 * @date : 2021-05-07
 **/

public class GreetingThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(Method method, Object[] args, Object target, Exception exception) {
        System.out.println("------------");
        System.out.println("method：" + method.getName());
        System.out.println("抛出异常：" + exception.getMessage());
        System.out.println("异常处理结束");
    }

}
