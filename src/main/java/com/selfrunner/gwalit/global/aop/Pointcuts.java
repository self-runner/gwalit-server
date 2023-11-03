package com.selfrunner.gwalit.global.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.selfrunner.gwalit.domain.*.service.*.*(..))")
    public void allService() {

    }
}
