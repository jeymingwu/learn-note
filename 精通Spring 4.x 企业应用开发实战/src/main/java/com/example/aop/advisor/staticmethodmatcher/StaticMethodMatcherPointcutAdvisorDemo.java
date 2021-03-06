package com.example.aop.advisor.staticmethodmatcher;

import com.example.aop.advisor.Seller;
import com.example.aop.advisor.Waiter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-08
 **/

public class StaticMethodMatcherPointcutAdvisorDemo {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/static-method-matcher-pointcut-advisor.xml");

        Seller seller = context.getBean("seller", Seller.class);
        Waiter waiter = context.getBean("waiter", Waiter.class);

        waiter.greetTo("John");
        waiter.serveTo("John");

        seller.greetTo("John");
    }
}
