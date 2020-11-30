package java.lang.reflect;

/**
 * @author jeymingwu
 * @date 2020/10/15 16:31
 */
public class Demo {

    public static void main(String[] args) {

        int c = 99;
        String a = "99", b = "" + c;

        System.out.println(a.equals(b));
    }
}
