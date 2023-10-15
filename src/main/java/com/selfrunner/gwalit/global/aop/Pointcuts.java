package com.selfrunner.gwalit.global.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.selfrunner.gwalit.domain.*.*Controller.*(..))")
    public void allController() {

    }

    @Pointcut("execution(* com.selfrunner.gwalit.domain.*.*Service.*(..))")
    public void allService() {

    }
}
