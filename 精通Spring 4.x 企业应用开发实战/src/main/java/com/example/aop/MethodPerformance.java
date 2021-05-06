package com.example.aop;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class MethodPerformance {

    private long begin;
    private long end;
    private String serviceMethod;

    public MethodPerformance(String serviceMethod) {
        this.serviceMethod = serviceMethod;
        this.begin = System.currentTimeMillis();
    }

    public void printPerformance() {
        this.end = System.currentTimeMillis();
        long elapse = this.end - this.begin;
        System.out.println(this.serviceMethod + " 花费 " + elapse + "毫秒。");
    }
}
