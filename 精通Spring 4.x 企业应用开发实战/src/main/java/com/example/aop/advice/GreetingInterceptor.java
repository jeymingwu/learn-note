package com.example.aop.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author : jeymingwu
 * @date : 2021-05-07
 **/

public class GreetingInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        // 目标方法入参
        Object[] arguments = methodInvocation.getArguments();

        String clientName = (String) arguments[0];

        // 前置增强
        System.out.println("How are you! Mr." + clientName + ".");

        // 通过反射机制调用目标方法
        Object proceed = methodInvocation.proceed();

        // 后置增强
        System.out.println("Please enjoy yourself!");

        return proceed;
    }
}
