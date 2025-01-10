package com.selfrunner.apimodule.global.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // application 패키지 내 모든 *.Service 클래스의 모든 메서드를 포인트컷으로 지정
    @Pointcut("execution(* com.selfrunner.apimodule.application..*Service.*(..))")
    public void allService() {
        // Pointcut signature
    }
}