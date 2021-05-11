package com.example.aop.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author : jeymingwu
 * @date : 2021-05-10
 **/

@Aspect
public class PreGreetingAspectJ {

    @Before("execution(* greetTo(..))")
    public void beforeGreeting() {
        System.out.println("How are you!");
    }
}
