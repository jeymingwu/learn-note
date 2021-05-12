package com.example.aop.aspectj.fun;

import com.example.aop.advice.Waiter;
import com.example.aop.aspectj.Seller;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-11
 **/

public class ThisDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/aspect/this-pointcut-factory-bean.xml");

        Waiter target = context.getBean("naiveWaiter", Waiter.class);

        target.greetTo("lisa");
        target.serveTo("lisa");

        Seller seller = (Seller) target;
        seller.sell("Beer", "lisa");

    }
}
