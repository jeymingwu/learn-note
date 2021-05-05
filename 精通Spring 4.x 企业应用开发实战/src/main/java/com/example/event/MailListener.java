package com.example.event;

import org.springframework.context.ApplicationListener;

/**
 * EventListener 事件监听器
 *
 * @author : jeymingwu
 * @date : 2021-05-05
 **/

public class MailListener implements ApplicationListener<MailSendEvent> {

    @Override
    public void onApplicationEvent(MailSendEvent mailSendEvent) {
        System.out.println("MailSendEvent：向 " + mailSendEvent.getTo() + " 发送一封邮件！");
    }

}
