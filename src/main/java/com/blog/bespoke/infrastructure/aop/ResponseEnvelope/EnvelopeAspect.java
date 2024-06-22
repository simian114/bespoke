package com.blog.bespoke.infrastructure.aop.ResponseEnvelope;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class EnvelopeAspect {
    @Around("execution(* com.blog.bespoke.presentation.web.controller..*(..))")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Envelope annotation = method.getAnnotation(Envelope.class);
        String msg = getMessage(annotation, methodName);
        Object result = joinPoint.proceed();

        if (!(result instanceof ResponseEntity<?> responseEntity)) {
            return result;
        }

        return EnvelopeResponse.envelope(
                responseEntity.getBody(),
                HttpStatus.valueOf(responseEntity.getStatusCode().value()),
                msg
        );
    }

    private String getMessage(Envelope envelopAnnotation, String methodName) {
        if (envelopAnnotation == null) {
            return methodName + " ok";
        }
        return envelopAnnotation.value();
    }
}
