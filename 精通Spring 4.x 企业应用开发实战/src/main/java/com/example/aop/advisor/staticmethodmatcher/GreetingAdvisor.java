package com.example.aop.advisor.staticmethodmatcher;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * 静态普通方法名匹配切面
 * 自定义匹配 Waiter#greetTo 方法
 *
 * @author : jeymingwu
 * @date : 2021-05-08
 **/

public class GreetingAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /**
     * 切点方法匹配规则，默认方法下匹配所有类
     * @param method
     * @param aClass
     * @return
     */
    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return "greetTo".equals(method.getName());
    }

    /**
     * 切点类匹配规则
     * @return
     */
    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> aClass) {
                return com.example.aop.advisor.staticmethodmatcher.Waiter.class.isAssignableFrom(aClass);
            }
        };
    }
}
