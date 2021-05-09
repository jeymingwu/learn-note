package com.example.aop.advisor.regexpmethod;

import com.example.aop.advisor.Seller;
import com.example.aop.advisor.Waiter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-09
 **/

public class RegexpMethodMatcherPointcutAdvisorDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/static-method-matcher-pointcut-advisor.xml");

        Seller seller = context.getBean("seller", Seller.class);
        Waiter waiter = context.getBean("regexpWaiter", Waiter.class);

        waiter.greetTo("John");
        waiter.serveTo("John");

        seller.greetTo("John");

    }
}
