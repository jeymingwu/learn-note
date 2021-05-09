package com.example.aop.advisor.controlflow;

import com.example.aop.advisor.Waiter;
import com.example.aop.advisor.WaiterDelegate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jeymingwu
 * @date : 2021-05-09
 **/

public class ControlFlowPointcutDemo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/flow-control-advisor.xml");

        Waiter waiter = context.getBean("waiter", Waiter.class);

        WaiterDelegate waiterDelegate = new WaiterDelegate();
        waiterDelegate.setWaiter(waiter);

        waiter.greetTo("Peter");
        waiter.serveTo("Peter");

        waiterDelegate.service("Peter");

    }
}
