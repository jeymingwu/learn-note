package com.example.aop.springaop;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class NaiveWaiter implements Waiter{
    @Override
    public void greetTo(String name) {
        System.out.println("greet to " + name + " ...");
    }

    @Override
    public void serveTo(String name) {
        System.out.println("serving to " + name + " ...");
    }
}
