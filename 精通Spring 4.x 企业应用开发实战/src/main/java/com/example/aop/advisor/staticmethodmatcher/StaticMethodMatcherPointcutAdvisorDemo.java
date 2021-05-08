package com.example.aop.advisor.staticmethodmatcher;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-08
 **/

public class StaticMethodMatcherPointcutAdvisorDemo {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/static-method-matcher-pointcut-advisor.xml");

        com.example.aop.advisor.staticmethodmatcher.Seller seller = context.getBean("seller", com.example.aop.advisor.staticmethodmatcher.Seller.class);
        com.example.aop.advisor.staticmethodmatcher.Waiter waiter = context.getBean("waiter", com.example.aop.advisor.staticmethodmatcher.Waiter.class);

        waiter.greetTo("John");
        waiter.serveTo("John");

        seller.greetTo("John");
    }
}
