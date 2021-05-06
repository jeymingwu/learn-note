package com.example.aop.cglibproxy;

import com.example.aop.PerformanceMonitor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class CGLibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        enhancer.setSuperclass(clazz); // 设置需要创建子类的类
        enhancer.setCallback(this);
        return enhancer.create();// 通过字节码技术动态创建子类实例
    }

    /**
     * 拦截所有父类的方法
     * @param obj 目标类实例
     * @param method 目标类方法的反射对象
     * @param args 方法的动态入参
     * @param proxy 代理类实例
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        PerformanceMonitor.begin(obj.getClass().getName() + "." + method.getName());

        // 执行父类的方法
        Object result = proxy.invokeSuper(obj, args);// 通过代理类调用父类中的方法

        PerformanceMonitor.end();

        return result;
    }
}
