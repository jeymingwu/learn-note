package mybatis.reflect.proxy;

/**
 * @author jeymingwu
 * @date 2020/9/22 18:28
 */
public class HelloServiceMain {

    public static void main(String[] args) {
        HelloServiceJdkProxy helloHandler = new HelloServiceJdkProxy();
        HelloService proxy = (HelloService) helloHandler.bind(new HelloServiceImpl());
        proxy.sayHello("张三");

        HelloServiceCgLibProxy cgLibProxy = new HelloServiceCgLibProxy();
        HelloServiceImpl instance = (HelloServiceImpl) cgLibProxy.getInstance(new HelloServiceImpl());
        instance.sayHello("李四");

    }
}
