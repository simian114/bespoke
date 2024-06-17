package com.blog.bespoke.infrastructure.aop.ResponseEnvelope;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
// NOTE: method 에 annotation 으로 하는게 좋을까 아니면 pointcut 으로 하는게 좋을까 생각해보기
public class ResponseEnvelopeAspect {
    @Around("execution(* com.blog.bespoke.presentation.web.controller..*(..))")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        // TODO: try & catch
        String methodName = joinPoint.getSignature().getName();
        Object result = joinPoint.proceed();
        if (!(result instanceof ResponseEntity<?> responseEntity)) {
            return result;
        }
        return ResponseEntity
                .status(responseEntity.getStatusCode())
                .body(
                        new ResponseEnvelope<>(responseEntity.getBody(),
                                (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()
                                        ? methodName + " error"
                                        : methodName + " ok"
                                ),
                                responseEntity.getStatusCode().value()
                        )
                );
    }
}
