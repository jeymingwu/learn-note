package com.example.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundle Demo
 *
 * @author : jeymingwu
 * @date : 2021-05-05
 **/

public class ResourceBundleDemo {

    public static void main(String[] args) {
        ResourceBundle us = ResourceBundle.getBundle("i18n", Locale.US);
        ResourceBundle cn = ResourceBundle.getBundle("il8n", Locale.CHINA);

        System.out.println("us:" + us.getString("greeting.common"));
        System.out.println("cn:" + cn.getString("greeting.common"));
    }
}
