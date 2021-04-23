package com.example.lifecycle;

import com.example.pojo.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author : jeymingwu
 * @date : 2021-04-22
 **/

public class LifeCycleBook extends Book implements BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean, ApplicationContextAware {

    private String beanName;
    private BeanFactory beanFactory;

    /**
     * 步骤 2
     */
    public LifeCycleBook() {
        System.out.println("步骤 2：调用构造方法");
    }

    public LifeCycleBook(int id, String name, double price) {
        super(id, name, price);
        System.out.println("步骤 2：调用构造方法");
    }

    /**
     * 步骤 7
     * BeanFactoryAware 接口方法
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("步骤 7：调用 BeanFactoryAware.setBeanFactory() ……");
        this.beanFactory = beanFactory;
    }

    /**
     * 步骤 6
     * BeanNameAware 接口方法
     * @param s
     */
    @Override
    public void setBeanName(String s) {
        System.out.println("步骤 6：调用 BeanNameAware.setBeanName() ……");
        this.beanName = s;
    }


    /**
     * 步骤 13
     * DisposableBean 接口方法
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("步骤 13：调用 DisposableBean.destroy() ……");
    }

    /**
     * 步骤 9
     * InitializingBean 接口方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("步骤 9：调用 InitializingBean.afterPropertiesSet() ……");
    }

    /**
     * 步骤 10
     * 调用 bean 中 init-method
     */
    public void myInit() {
        System.out.println("步骤 10：调用 init-method ……");
    }

    /**
     * 步骤 14
     * 调用 bean 中 destroy-method
     */
    public void myDestroy() {
        System.out.println("步骤 14：调用 destroy-method ……");
    }

    @Override
    public void setId(int id) {
        System.out.println("步骤 5：调用 setter ……");
        super.setId(id);
    }

    @Override
    public void setName(String name) {
        System.out.println("步骤 5：调用 setter ……");
        super.setName(name);
    }

    @Override
    public void setPrice(double price) {
        System.out.println("步骤 5：调用 setter ……");
        super.setPrice(price);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("步骤 7+1：调用 ApplicationContextAware.setApplicationContext……");
    }
}
