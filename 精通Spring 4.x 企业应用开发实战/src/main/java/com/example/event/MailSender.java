package com.example.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author : jeymingwu
 * @date : 2021-05-05
 **/

public class MailSender implements ApplicationContextAware {

    private ApplicationContext context;

    /**
     * Bean 初始化时注入容器实例，方便调用
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void sendMail(String to) {
        System.out.println("模拟开始发送邮件……");

        MailSendEvent mailSendEvent = new MailSendEvent(this.context, to);

        // 向容器中所有事件监听器发送事件
        context.publishEvent(mailSendEvent);
    }
}
