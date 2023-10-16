package com.selfrunner.gwalit.global.aop;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    // 비즈니스 로직의 실행시간 기록
    @Around("com.selfrunner.gwalit.global.aop.Pointcuts.allService()")
    public Object executionTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Long startTime = System.currentTimeMillis();
            String method = joinPoint.getSignature().toString();
            Object result = joinPoint.proceed();

            Long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            if(executionTime < 1000) {
                log.info("[" + method + "] " + " execution time: " + executionTime + "ms");
            }
            else {
                log.warn("[" + method + "] " + " execution time: " + executionTime + "ms");
                Sentry.captureMessage("Slow Query: " + "[" + method + "] " + " execution time: " + executionTime + "ms");
            }

            return result;
        } catch (ApplicationException e) {
            String method = joinPoint.getSignature().toString();
            log.error("[" + method + "] " + " Application Exception: " + e.getErrorCode().toString());
            throw e;
        } catch (Exception e) {
            String method = joinPoint.getSignature().toString();
            log.error("[" + method + "] " + " Application Exception: " + e.getMessage());
            throw e;
        }
    }
}
