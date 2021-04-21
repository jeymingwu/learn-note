package com.example.bean;

import com.example.pojo.Book;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * Bean 初始化模拟
 * @author : jeymingwu
 * @date : 2021-04-20
 **/
public class BeanFactoryDemo {

    public void getBean() throws IOException {

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("BeanFactoryTest.xml");
        System.out.println(resource.getURL());

        // 被废弃
//        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(resource);

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);

        System.out.println("init BeanFactory……");

        Book book = factory.getBean("book", Book.class);
        System.out.println("bean is ready!");
        System.out.println(book);
    }

    public static void main(String[] args) throws IOException {
        BeanFactoryDemo beanFactoryDemo = new BeanFactoryDemo();
        beanFactoryDemo.getBean();
    }
}
