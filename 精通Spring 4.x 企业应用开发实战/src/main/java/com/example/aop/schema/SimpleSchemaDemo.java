package com.example.aop.schema;

import com.example.aop.advice.Waiter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-12
 **/

public class SimpleSchemaDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/schema/simple-schema-factory-bean.xml");

        Waiter naiveWaiter = context.getBean("naiveWaiter", Waiter.class);
        Waiter naughtyWaiter = context.getBean("naughtyWaiter", Waiter.class);

        naiveWaiter.greetTo("Lisa");

        naughtyWaiter.greetTo("Tom");

    }
}
