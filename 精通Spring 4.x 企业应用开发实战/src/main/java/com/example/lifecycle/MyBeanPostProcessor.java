package com.example.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author : jeymingwu
 * @date : 2021-04-22
 **/

public class MyBeanPostProcessor implements BeanPostProcessor {

    /**
     * 步骤 8
     * @param o
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {

        if (s.equals("lifeCycleBook")) {
            System.out.println("步骤 8: BeanPostProcess.postProcessBeforeInstantiation");
        }

        return o;
    }

    /**
     * 步骤 11
     * @param o
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {

        if (s.equals("lifeCycleBook")) {
            System.out.println("步骤 11: BeanPostProcess.postProcessAfterInitialization");
        }

        return o;
    }
}
