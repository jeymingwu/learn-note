package com.example.bean;

import com.example.pojo.ParentBook;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-04-25
 **/

public class ParentBeanDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext parentApplicationContext = new ClassPathXmlApplicationContext(new String[]{"BeanFactoryTest.xml"});

        ClassPathXmlApplicationContext sonApplicationContext = new ClassPathXmlApplicationContext(new String[]{"parent-bean-test.xml"}, parentApplicationContext);

        ParentBook parentBook = sonApplicationContext.getBean("parentBook", ParentBook.class);
        System.out.println(parentBook);

    }
}
