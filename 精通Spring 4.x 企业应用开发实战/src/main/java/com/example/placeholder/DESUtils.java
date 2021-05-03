package com.example.placeholder;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * DES 加密解密工具类
 * @author : jeymingwu
 * @date : 2021-05-03
 **/

public class DESUtils {

    // 1.指定 DES 加密解密所用的密钥
    private static Key key;
    private static String KEY_STR = "myKey";

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            generator.init(new SecureRandom(KEY_STR.getBytes()));
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 2.对字符串进行 DES 加密，返回 BASE64 编码的加密字符串
    public static String getEncryptString(String str) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            byte[] strBytes = str.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(strBytes);
            return base64Encoder.encode(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 3.对 BASE64 编码的加密字符串进行解密，返回解密后的字符串
    public static String getDecryptString(String str) {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            byte[] strBytes = base64Decoder.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptStrBytes = cipher.doFinal(strBytes);
            return new String(decryptStrBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
//        if (args == null || args.length < 1) {
//            System.out.println("input value is null");
//        } else {
//            for (String arg : args) {
//                System.out.println(arg + " : " + getEncryptString(arg));
//            }
//        }

        System.out.println(getEncryptString("root"));
        System.out.println(getEncryptString("123456"));
    }
}
