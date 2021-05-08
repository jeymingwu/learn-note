package com.example.aop.advice.introduction;

import com.example.aop.ForumService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-07
 **/

public class IntroductionDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/introduction-advice-factory-bean.xml");

        ForumService forumService = context.getBean("forumService", ForumService.class);

        forumService.removeForum(10);
        forumService.removeTopic(1024);

        Monitored monitored = (Monitored) forumService;
        monitored.setMonitorAdvice(true);

        forumService.removeForum(10);
        forumService.removeTopic(1024);

    }
}
