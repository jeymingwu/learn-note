package com.example.aop.aspectj;

import com.example.aop.advice.Waiter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-11
 **/

public class DeclareParentsDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/aspect/aspect-factory-bean.xml");

        Waiter target = context.getBean("target", Waiter.class);

        target.greetTo("lisa");
        target.serveTo("lisa");

        Seller seller = (Seller) target;
        seller.sell("Beer", "lisa");

    }
}
