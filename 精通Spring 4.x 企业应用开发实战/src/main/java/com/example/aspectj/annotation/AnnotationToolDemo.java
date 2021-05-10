package com.example.aspectj.annotation;

import java.lang.reflect.Method;

/**
 * @author : jeymingwu
 * @date : 2021-05-10
 **/

public class AnnotationToolDemo {

    public static void main(String[] args) {

        Class<ForumService> forumServiceClass = ForumService.class;

        Method[] methods = forumServiceClass.getDeclaredMethods();

        System.out.println(methods.length);

        for (Method method:methods) {
            NeedTest annotation = method.getAnnotation(NeedTest.class);
            if (annotation != null) {
                if (annotation.value()) {
                    System.out.println(method.getName() + "()需要测试");
                } else {
                    System.out.println(method.getName() + "()不需要测试");
                }
            }
        }
    }

}
