package com.example.aop.autoproxy;

import com.example.aop.advisor.Seller;
import com.example.aop.advisor.Waiter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-10
 **/

public class BeanNameAutoProxyCreatorDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/autoproxy/bean-name-auto-proxy.xml");

        Waiter waiter = context.getBean("waiter", Waiter.class);
        Seller seller = context.getBean("seller", Seller.class);

        waiter.greetTo("Tony");
        waiter.serveTo("Tony");

        seller.greetTo("Lisa");

    }
}
