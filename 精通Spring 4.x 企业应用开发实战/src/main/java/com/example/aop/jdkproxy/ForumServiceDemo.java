package com.example.aop.jdkproxy;

import com.example.aop.ForumService;
import com.example.aop.ForumServiceImpl;

import java.lang.reflect.Proxy;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class ForumServiceDemo {

    public static void main(String[] args) {

        // 被代理的目标业务类
        ForumService target = new ForumServiceImpl();

        // 将目标业务类与横切代码编织一起
        PerformanceHandler handler = new PerformanceHandler(target);

        ForumService proxy = (ForumService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                handler
        );

        proxy.removeForum(10);
        proxy.removeTopic(1021);
    }
}
