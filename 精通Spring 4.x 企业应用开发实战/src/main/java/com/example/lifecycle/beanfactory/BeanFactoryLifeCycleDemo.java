package com.example.lifecycle.beanfactory;

import com.example.lifecycle.LifeCycleBook;
import com.example.lifecycle.MyBeanPostProcessor;
import com.example.lifecycle.MyInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author : jeymingwu
 * @date : 2021-04-22
 **/

public class BeanFactoryLifeCycleDemo {

    private static void lifeCycleInBeanFactory() {

        // 加载 Resource
        Resource res = new ClassPathResource("BeanFactoryTest.xml");

//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource res = resolver.getResource("BeanFactoryTest.xml");

        // BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(res);

        // 向 BeanFactory 注册后处理器
        ((ConfigurableBeanFactory) beanFactory).addBeanPostProcessor(new MyBeanPostProcessor());
        ((ConfigurableBeanFactory) beanFactory).addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());

        // 向 BeanFactory 获取 Bean 实例
        LifeCycleBook lifeCycleBook = beanFactory.getBean("lifeCycleBook", LifeCycleBook.class);
        System.out.println(lifeCycleBook.toString());
        lifeCycleBook.setPrice(200.0);

        // 验证 scope=“singleton” 作用域下，Bean 的实例化情况
        LifeCycleBook otherLifeCycleBook = beanFactory.getBean("lifeCycleBook", LifeCycleBook.class);
        System.out.println("lifeCycleBook == otherLifeCycleBook：" + (lifeCycleBook == otherLifeCycleBook));

        // 销毁 BeanFactory
        ((ConfigurableBeanFactory) beanFactory).destroySingletons();
    }

    public static void main(String[] args) {
        BeanFactoryLifeCycleDemo.lifeCycleInBeanFactory();
    }
}
