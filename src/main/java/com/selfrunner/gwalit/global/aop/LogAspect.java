package com.selfrunner.gwalit.global.aop;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    // 호출된 API 정보 기록
    @Before("com.selfrunner.gwalit.global.aop.Pointcuts.allController()")
    public Object apiLog(ProceedingJoinPoint joinPoint) throws Throwable {

    }

    // 비즈니스 로직의 실행시간 기록
    @Around("com.selfrunner.gwalit.global.aop.Pointcuts.allService()")
    public Object executionTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();

            return result;
        } catch (ApplicationException e) {

        } catch (Exception e) {

        }
    }
}
