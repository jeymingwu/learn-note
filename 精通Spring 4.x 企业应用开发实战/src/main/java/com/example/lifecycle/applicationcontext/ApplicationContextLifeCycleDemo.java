package com.example.lifecycle.applicationcontext;

import com.example.lifecycle.LifeCycleBook;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-04-23
 **/

public class ApplicationContextLifeCycleDemo {

    private static void lifeCycleInBeanFactory() {

        // 加载 Resource
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("BeanFactoryTest.xml");

        // 向 BeanFactory 获取 Bean 实例
        LifeCycleBook lifeCycleBook = applicationContext.getBean("lifeCycleBook", LifeCycleBook.class);
        System.out.println(lifeCycleBook.toString());
        lifeCycleBook.setPrice(200.0);

        // 验证 scope=“singleton” 作用域下，Bean 的实例化情况
        LifeCycleBook otherLifeCycleBook = applicationContext.getBean("lifeCycleBook", LifeCycleBook.class);
        System.out.println("lifeCycleBook == otherLifeCycleBook：" + (lifeCycleBook == otherLifeCycleBook));

        // 销毁 ApplicationContext
        applicationContext.destroy();
    }

    public static void main(String[] args) {
        ApplicationContextLifeCycleDemo.lifeCycleInBeanFactory();
    }
}
