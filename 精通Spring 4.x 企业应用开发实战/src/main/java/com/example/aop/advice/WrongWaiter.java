package com.example.aop.advice;

/**
 * @author : jeymingwu
 * @date : 2021-05-07
 **/

public class WrongWaiter implements Waiter{

    public void wrong() {
        System.out.println("出现异常");
        throw new RuntimeException("员工身体突然不适");
    }

    @Override
    public void greetTo(String name) {
        System.out.println("greet to " + name + " ...");
        wrong();
    }

    @Override
    public void serveTo(String name) {
        System.out.println("serving to " + name + " ...");
        wrong();
    }
}
