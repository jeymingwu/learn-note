package com.example.aop.aspectj.fun.annotation;

import com.example.aop.advice.Waiter;
import com.example.aop.aspectj.annotation.NeedTest;

/**
 * @author : jeymingwu
 * @date : 2021-05-11
 **/

public class NaughtyWaiter implements Waiter {
    @NeedTest
    @Override
    public void greetTo(String name) {
        System.out.println(this.getClass().getName() + ": greet to " + name + "...");
    }

    @Override
    public void serveTo(String name) {
        System.out.println(this.getClass().getName() + ": serve to " + name + "...");
    }
}
