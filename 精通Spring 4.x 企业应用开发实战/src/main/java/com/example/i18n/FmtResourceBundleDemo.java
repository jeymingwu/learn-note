package com.example.i18n;

import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * format ResourceBundle Demo
 *
 * @author : jeymingwu
 * @date : 2021-05-05
 **/

public class FmtResourceBundleDemo {

    public static void main(String[] args) {
        ResourceBundle us = ResourceBundle.getBundle("fmt_i18n", Locale.US);
        ResourceBundle cn = ResourceBundle.getBundle("fmt_i18n", Locale.CHINA);

        Object[] params = {"Tony", new GregorianCalendar().getTime()};

        String str1 = new MessageFormat(us.getString("greeting.common"), Locale.US).format(params);
        String str2 = new MessageFormat(cn.getString("greeting.morning"), Locale.CHINA).format(params);
        String str3 = new MessageFormat(cn.getString("greeting.afternoon"), Locale.CHINA).format(params);

        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
    }
}
