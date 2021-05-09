package com.example.aop.advisor.dynamicmethodmatcher;

import com.example.aop.advisor.Waiter;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : jeymingwu
 * @date : 2021-05-09
 **/

public class GreetingDynamicPointcut extends DynamicMethodMatcherPointcut {

    private static List<String> specialClientList = new ArrayList<>();

    static {
        specialClientList.add("John");
        specialClientList.add("Tom");
    }

    /**
     * 对类的静态检查
     * @return
     */
    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> aClass) {
                System.out.println("调用 getClassFilter() 对 " + aClass.getName() + " 做静态检查。");
                return Waiter.class.isAssignableFrom(aClass);
            }
        };
    }

    /**
     * 对方法的静态检查
     * @param method
     * @param targetClass
     * @return
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        System.out.println("调用 matches(method, targetClass) " + targetClass.getName() + "." + method.getName() + " 做静态检查。");
        return "greetTo".equals(method.getName());
    }

    /**
     * 对方法入参的动态检查
     *
     * @param method
     * @param targetClass
     * @param objects
     * @return
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... objects) {
        System.out.println("调用 matches(method, targetClass, args) " + targetClass.getName() + "." + method.getName() + " 做动态检查。");
        String clientName = (String) objects[0];
        return specialClientList.contains(clientName);
    }

}
