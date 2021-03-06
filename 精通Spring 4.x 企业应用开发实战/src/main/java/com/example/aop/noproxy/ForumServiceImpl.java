package com.example.aop.noproxy;

import com.example.aop.ForumService;
import com.example.aop.PerformanceMonitor;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class ForumServiceImpl implements ForumService {
    @Override
    public void removeTopic(int topicId) {
        PerformanceMonitor.begin(this.getClass().toString() + ".removeTopic");
        System.out.println("模拟删除 Topic 记录：" + topicId);
        try {
            Thread.currentThread().sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PerformanceMonitor.end();
    }

    @Override
    public void removeForum(int forumId) {
        PerformanceMonitor.begin(this.getClass().toString()+ ".removeForum");
        System.out.println("模拟删除 Forum 记录：" + forumId);
        try {
            Thread.currentThread().sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PerformanceMonitor.end();
    }
}
