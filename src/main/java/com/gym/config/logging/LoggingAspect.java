package com.gym.config.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.gym.services..*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        log.info(
                "START operation: {} arguments: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs()
        );
    }

    @AfterReturning(pointcut = "execution(* com.gym.services..*(..))", returning = "result")
    public void afterSuccess(JoinPoint joinPoint, Object result) {
        log.info(
                "SUCCESS operation: {} response: {}",
                joinPoint.getSignature().getName(),
                result
        );
    }

    @AfterThrowing(pointcut = "execution(* com.gym.services..*(..))", throwing = "exception")
    public void afterError(JoinPoint joinPoint, Exception exception) {
        log.error(
                "ERROR operation: {} message: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage()
        );
    }
}
