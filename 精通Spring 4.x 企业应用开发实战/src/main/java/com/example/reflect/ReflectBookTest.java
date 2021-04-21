package com.example.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射生成 Book 对象
 */
public class ReflectBookTest {

    public static Object initInstance(int id, String name, double price) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // 通过类加载器获取对象
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?> clazz = loader.loadClass("com.example.pojo.Book");

        // 通过获取类的无参构造器构建实例
        Constructor<?> constructor = clazz.getDeclaredConstructor(null);
        Object instance = constructor.newInstance();

        // 通过反射方法设置属性
        Method setId = clazz.getMethod("setId", int.class);
        setId.invoke(instance, id);
        Method setName = clazz.getMethod("setName", String.class);
        setName.invoke(instance, name);
        Method setPrice = clazz.getMethod("setPrice", double.class);
        setPrice.invoke(instance, price);

        return instance;
    }

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object instance = ReflectBookTest.initInstance(1, "Java 核心技术", 100.00);
        System.out.println(instance);
    }
}
