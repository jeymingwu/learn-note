package com.example.aop.springaop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class AdviceWithXmlDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/ProxyFactoryBean.xml");

        // 前置、后置增强
        System.out.println("前置、后置增强测试");
        Waiter waiter = context.getBean("waiter", Waiter.class);
        waiter.greetTo("Tom");

        // 环绕增强
        System.out.println("环绕增强测试");
        Waiter waiterByIntercept = context.getBean("waiterByIntercept", Waiter.class);
        waiterByIntercept.greetTo("Lisa");

        // 异常抛出增强
        System.out.println("异常抛出增强测试");
        Waiter wrongWaiter = context.getBean("waiterByThrows", Waiter.class);
        wrongWaiter.greetTo("Jenny");
    }
}
