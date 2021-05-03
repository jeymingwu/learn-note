package com.example.placeholder;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 继承 PropertyPlaceholderConfiguration 定义支持密文版属性的属性配置器
 * @author : jeymingwu
 * @date : 2021-05-03
 **/
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private String[] encryptPropNames = {"username", "password"};

    /**
     * 对特定属性的属性值进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            String decryptValue = DESUtils.getDecryptString(propertyValue);
            System.out.println(propertyValue + " : " + decryptValue);
            return decryptValue;
        }
        return propertyValue;
    }

    /**
     * 判断是否需要解密的属性
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName) {
        for (String encryptPropName:encryptPropNames) {
            if (encryptPropName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
