package com.example.event;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-05
 **/

public class MailSenderMain {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-event-bean.xml");

        MailSender mailSender = context.getBean("mailSender", MailSender.class);

        mailSender.sendMail("lisa");
    }
}
