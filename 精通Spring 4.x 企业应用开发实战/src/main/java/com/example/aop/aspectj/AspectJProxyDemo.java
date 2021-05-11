package com.example.aop.aspectj;

import com.example.aop.advice.NaiveWaiter;
import com.example.aop.advice.Waiter;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-11
 **/

public class AspectJProxyDemo {

    public static void main(String[] args) {
        // 通过定义使用 @Aspect 切面
//        Waiter target = new NaiveWaiter();
//
//        AspectJProxyFactory factory = new AspectJProxyFactory();
//        // 设置目标对象
//        factory.setTarget(target);
//        // 添加切面类
//        factory.addAspect(PreGreetingAspectJ.class);
//        // 生成织入切面的代理对象
//        Waiter proxy = factory.getProxy();

        // 通过配置使用 @Aspect 切面
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/aspect/aspect-factory-bean.xml");

        Waiter proxy = context.getBean("target", Waiter.class);

        proxy.greetTo("Lisa");
        proxy.serveTo("Lisa");

    }
}
