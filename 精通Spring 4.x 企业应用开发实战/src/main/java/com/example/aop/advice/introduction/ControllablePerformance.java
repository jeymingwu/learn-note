package com.example.aop.advice.introduction;

import com.example.aop.PerformanceMonitor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * 引介增强 Demo
 * 为一个类实现一个接口
 *
 * @author : jeymingwu
 * @date : 2021-05-07
 **/

public class ControllablePerformance extends DelegatingIntroductionInterceptor implements Monitored {

    private ThreadLocal<Boolean> monitorStatusMap = new ThreadLocal<>();

    @Override
    public void setMonitorAdvice(boolean active) {
        monitorStatusMap.set(active);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object obj = null;

        if (this.monitorStatusMap.get() != null && monitorStatusMap.get()) {
            PerformanceMonitor.begin(methodInvocation.getClass().getName() + "." + methodInvocation.getMethod().getName());
            obj = super.invoke(methodInvocation);
            PerformanceMonitor.end();
        } else {
            obj = super.invoke(methodInvocation);
        }
        return obj;
    }
}
