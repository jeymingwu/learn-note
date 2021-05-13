package com.example.spel.jvm;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author : jeymingwu
 * @date : 2021-05-13
 **/

public class ScriptDemo {

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {

        String scriptText = "function sum(a, b) { return a + b; }";

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.eval(scriptText);
        Invocable invocable = (Invocable) engine;
        Object result = invocable.invokeFunction("sum", 100, 200);
        System.out.println(result);
    }
}
