package com.example.aop.advisor.dynamicmethodmatcher;

import com.example.aop.advisor.Seller;
import com.example.aop.advisor.Waiter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-09
 **/

public class DynamicAdvisorDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/dynamic-method-matcher-pointcut-advisor.xml");

        Waiter waiter = context.getBean("waiter", Waiter.class);
        Seller seller = context.getBean("seller", Seller.class);

        waiter.greetTo("Lisa");
        waiter.serveTo("Lisa");

        seller.greetTo("GeeLee");
        seller.greetTo("GeeLee");

        waiter.greetTo("John");
        waiter.serveTo("John");
    }
}
