package com.example.bean;

import com.example.pojo.Book;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 由 FactoryBean 接口实现的工厂 Demo
 *
 * @author : jeymingwu
 * @date : 2021-04-27
 **/

public class FactoryBeanDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("FactoryBeanTest.xml");

        Book bean = classPathXmlApplicationContext.getBean("bookFactoryBean", Book.class);
        System.out.println(bean);

        Book otherBean = classPathXmlApplicationContext.getBean("bookFactoryBean", Book.class);
        System.out.println(otherBean);
        System.out.println("bean == otherBean : " + (bean == otherBean));

    }
}
