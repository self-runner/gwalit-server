package com.selfrunner.apimodule.global.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.selfrunner.commonmodule.domain.*.service.*.*(..))")
    public void allService() {

    }
}
