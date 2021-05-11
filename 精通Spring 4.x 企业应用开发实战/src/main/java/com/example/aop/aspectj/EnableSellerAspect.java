package com.example.aop.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

/**
 * @author : jeymingwu
 * @date : 2021-05-11
 **/
@Aspect
public class EnableSellerAspect {

    @DeclareParents(value = "com.example.aop.advice.NaiveWaiter", // 添加要实现的接口（目标类）
            defaultImpl = SmartSeller.class) // 默认的接口实现类（需要目标类实现的接口的实现类）
    public Seller seller;
}
