package com.example.aop.cglibproxy;

import com.example.aop.ForumService;
import com.example.aop.jdkproxy.ForumServiceImpl;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class ForumServiceDemo {

    public static void main(String[] args) {

        CGLibProxy proxy = new CGLibProxy();

        ForumService forumService = (ForumService) proxy.getProxy(ForumServiceImpl.class);

        forumService.removeForum(10);

        forumService.removeTopic(1023);
    }
}
