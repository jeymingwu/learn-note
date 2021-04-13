package mybatis.reflect.proxy;

/**
 * @author jeymingwu
 * @date 2020/9/22 18:29
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        System.out.println("hello " + name);
    }
}
