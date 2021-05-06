package com.example.aop;

/**
 * @author : jeymingwu
 * @date : 2021-05-06
 **/

public class PerformanceMonitor {

    private static ThreadLocal<MethodPerformance> performanceRecord = new ThreadLocal<>();

    public static void begin(String method) {
        System.out.println("begin monitor……");
        MethodPerformance methodPerformance = new MethodPerformance(method);
        performanceRecord.set(methodPerformance);
    }

    public static void end() {
        System.out.println("end monitor……");
        MethodPerformance methodPerformance = performanceRecord.get();
        methodPerformance.printPerformance();
    }
}
