package com.example.aop.springaop;

import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class BeforeAdviceDemo {

    public static void main(String[] args) {
        Waiter waiter = new NaiveWaiter();

        BeforeAdvice advice = new GreetingBeforeAdvice();

        // 代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();
        // 设置代理目标
        proxyFactory.setTarget(waiter);
        // 为代理目标添加增强
        proxyFactory.addAdvice(advice);
        // 生成代理实例
        Waiter proxy = (Waiter) proxyFactory.getProxy();
        proxy.greetTo("John");
        proxy.serveTo("Tom");

    }
}
