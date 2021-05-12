package com.example.aop.aspectj.fun.annotation;

import com.example.aop.advice.NaiveWaiter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-11
 **/

public class AnnotationPointcutDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/aspect/annotation-pointcut-factory-bean.xml");

        NaiveWaiter naiveWaiter = context.getBean("naiveWaiter", NaiveWaiter.class);
        NaughtyWaiter naughtyWaiter = context.getBean("naughtyWaiter", NaughtyWaiter.class);

        naiveWaiter.greetTo("Lisa");
        naiveWaiter.serveTo("Lisa");

        naughtyWaiter.greetTo("Tony");
        naughtyWaiter.serveTo("Tony");
    }
}
