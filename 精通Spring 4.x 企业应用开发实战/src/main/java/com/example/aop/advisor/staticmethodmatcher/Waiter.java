package com.example.aop.advisor.staticmethodmatcher;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class Waiter{
    public void greetTo(String name) {
        System.out.println("greet to " + name + " ...");
    }

    public void serveTo(String name) {
        System.out.println("serving to " + name + " ...");
    }
}
