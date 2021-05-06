package com.example.aop.noproxy;

import com.example.aop.ForumService;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class ForumServiceDemo {

    public static void main(String[] args) {
        ForumService forumService = new ForumServiceImpl();
        forumService.removeForum(20);
        forumService.removeTopic(1020);

    }
}
