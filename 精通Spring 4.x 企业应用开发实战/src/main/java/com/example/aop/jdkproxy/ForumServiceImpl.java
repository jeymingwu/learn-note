package com.example.aop.jdkproxy;

import com.example.aop.ForumService;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class ForumServiceImpl implements ForumService {
    @Override
    public void removeTopic(int topicId) {
        System.out.println("模拟删除 Topic 记录：" + topicId);
        try {
            Thread.currentThread().sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeForum(int forumId) {
        System.out.println("模拟删除 Forum 记录：" + forumId);
        try {
            Thread.currentThread().sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
