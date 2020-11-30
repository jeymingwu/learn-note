package mybatis.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理
 * @author jeymingwu
 * @date 2020/9/22 18:19
 */
public class HelloServiceJdkProxy implements InvocationHandler {

    private Object target;

    public Object bind(Object target) {
        this.target = target;
        // 三个参数：类加载器、类的接口、执行当前被代理类的对象（代理类的实例对象）
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    /**
     * 通过代理对象调用
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("======== JDK 动态代理 =========");
        Object result = null;

        // 方法调用前
        System.out.println("方法调用前");
        // 方法调用
        result = method.invoke(target, args);
        // 方法调用后
        System.out.println("方法调用后");
        return result;
    }
}
