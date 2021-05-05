package com.example.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Spring 下对国际资源文件的处理
 * MessageSource
 * ResourceBundleMessageSource （配置资源）
 *
 * @author : jeymingwu
 * @date : 2021-05-05
 **/

public class ResourceBundleMessageSourceDemo {

    public static void main(String[] args) {
        String[] configs = {"i18n.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configs);

        MessageSource i18n = context.getBean("i18n", MessageSource.class);
        Object[] params = {"Tony", new GregorianCalendar().getTime()};

        String str1 = i18n.getMessage("greeting.common", params, Locale.US);
        String str2 = i18n.getMessage("greeting.morning", params, Locale.CHINA);
        String str3 = i18n.getMessage("greeting.afternoon", params, Locale.CHINA);

        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
    }
}
