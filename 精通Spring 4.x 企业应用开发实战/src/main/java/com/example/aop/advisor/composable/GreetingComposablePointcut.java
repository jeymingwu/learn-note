package com.example.aop.advisor.composable;

import com.example.aop.advisor.WaiterDelegate;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.ControlFlowPointcut;
import org.springframework.aop.support.NameMatchMethodPointcut;

/**
 * @author : jeymingwu
 * @date : 2021-05-09
 **/

public class GreetingComposablePointcut {

    public Pointcut getIntersectionPointcut() {

        ComposablePointcut composablePointcut = new ComposablePointcut();

        Pointcut flowPointcut = new ControlFlowPointcut(WaiterDelegate.class, "service");

        Pointcut matchMethodPointcut = new NameMatchMethodPointcut();
        ((NameMatchMethodPointcut) matchMethodPointcut).addMethodName("greetTo");

        return composablePointcut.intersection(flowPointcut).intersection(matchMethodPointcut);

    }
}
