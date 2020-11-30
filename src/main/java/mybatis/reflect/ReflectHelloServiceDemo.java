package mybatis.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射 Demo
 * @author jeymingwu
 * @date 2020/9/22 18:03
 */
public class ReflectHelloServiceDemo {

    public void sayHello(String name) {
        System.out.println("hello " + name);
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // 通过反射创建 ReflectService 对象
        Object object = Class.forName(ReflectHelloServiceDemo.class.getName()).newInstance();

        // 获取服务方法 —— sayHello()
        Method method = object.getClass().getMethod("sayHello", String.class);

        // 反射调用方法
        method.invoke(object, "tony");
    }
}
