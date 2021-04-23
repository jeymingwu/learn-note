package com.example.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.beans.PropertyDescriptor;

/**
 * @author : jeymingwu
 * @date : 2021-04-22
 **/

public class MyInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    /**
     * 步骤 1
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (beanName.equals("lifeCycleBook")) {
            System.out.println("步骤 1：Instantiation BeanPostProcessor.postProcessBeforeInstantiation");
        }

        return null;
    }

    /**
     * 步骤 3
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {

        if (beanName.equals("lifeCycleBook")) {
            System.out.println("步骤 3：Instantiation BeanPostProcessor.postProcessAfterInstantiation");
        }

        return true;
    }

    /**
     * 步骤 4
     * @param pvs
     * @param pds
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        if (beanName.equals("lifeCycleBook")) {
            System.out.println("步骤 4：Instantiation BeanPostProcessor.postProcessPropertyValues");
        }

        return pvs;
    }
}
