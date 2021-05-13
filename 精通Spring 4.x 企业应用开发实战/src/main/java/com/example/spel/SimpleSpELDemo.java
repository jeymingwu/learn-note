package com.example.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author : jeymingwu
 * @date : 2021-05-13
 **/

public class SimpleSpELDemo {

    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("'Hello ' + 'world'");
        String message = (String) expression.getValue();
        System.out.println(message);;
    }
}
