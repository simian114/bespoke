package com.blog.bespoke.infrastructure.aop.trace;

import org.aspectj.lang.annotation.Pointcut;

public class TracePointcuts {
    @Pointcut("execution(* com.blog.bespoke.presentation.web.controller.*.*(..))")
    public void methodsFromController(){}

    @Pointcut("execution(* com.blog.bespoke.application.usecase.*.*(..))")
    public void methodsFromUseCase() {}
}
