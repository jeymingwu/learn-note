package com.example.placeholder;

import com.example.pojo.Account;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-03
 **/

public class PlaceholderMain {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("placeholder.xml");
        Account account = applicationContext.getBean("account", Account.class);
        System.out.println(account);

    }
}
