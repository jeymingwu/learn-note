package mybatis.reflect.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author jeymingwu
 * @date 2020/9/22 18:38
 */
public class HelloServiceCgLibProxy implements MethodInterceptor {

    private Object target;

    /**
     * 创建代理对象
     * @param target
     * @return
     */
    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    /**
     * 回调方法
     * @param object
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("======== CgLib 动态代理 =========");

        // 方法调用前
        System.out.println("方法调用前");
        // 方法调用
        Object obj = methodProxy.invokeSuper(object, args);
        // 方法调用后
        System.out.println("方法调用后");
        return obj;
    }
}
