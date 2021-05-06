package com.example.aop.jdkproxy;

import com.example.aop.PerformanceMonitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class PerformanceHandler implements InvocationHandler {

    private Object target;

    public PerformanceHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PerformanceMonitor.begin(target.getClass().getName() + "." + method.getName());
        Object invoke = method.invoke(target, args);
        PerformanceMonitor.end();
        return invoke;
    }
}
