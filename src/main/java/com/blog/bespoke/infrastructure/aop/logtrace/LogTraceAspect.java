package com.blog.bespoke.infrastructure.aop.logtrace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Aspect
public class LogTraceAspect {
    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }
    //execution(* com.blog.bespoke.presentation.web.controller..*(..))
    @Around("execution(* com.blog.bespoke.presentation.web.controller..*(..)) || execution(* com.blog.bespoke.application.usecase..*(..)) || execution(* com.blog.bespoke.domain.service..*(..))")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature()
                    .toShortString()
                    .replace("..", formatArguments(joinPoint.getArgs()));
            status = logTrace.begin(message);

            Object result = joinPoint.proceed();

            logTrace.end(status, result == null ? "" : result.toString());
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    private String formatArguments(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) {
                        return "null";
                    }
                    return arg.toString();
                })
                .collect(Collectors.joining(", "));
    }
}
