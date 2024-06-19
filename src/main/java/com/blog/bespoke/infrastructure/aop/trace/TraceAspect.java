package com.blog.bespoke.infrastructure.aop.trace;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class TraceAspect {
    private final Trace trace;

    @Around("com.blog.bespoke.infrastructure.aop.trace.TracePointcuts.methodsFromController() || com.blog.bespoke.infrastructure.aop.trace.TracePointcuts.methodsFromUseCase()")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature()
                    .toShortString()
                    .replace("..", formatArguments(joinPoint.getArgs()));
            status = trace.begin(message);

            Object result = joinPoint.proceed();

            trace.end(status, result == null ? "" : result.toString());
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
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
