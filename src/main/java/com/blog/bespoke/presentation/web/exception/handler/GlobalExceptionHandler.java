package com.blog.bespoke.presentation.web.exception.handler;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.EnvelopeResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        return EnvelopeResponse.envelope(e.getStatusCode(), e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleBeanValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    FieldError fieldError = (FieldError) error;
                    return String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage());
                })
                .collect(Collectors.joining("\n"));
        return EnvelopeResponse.envelope(HttpStatus.BAD_REQUEST, errorMessage, request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUnHandledException(Exception e, HttpServletRequest request) {
        log.error("", e);
        return EnvelopeResponse.envelope(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 생겼습니다.", request.getRequestURI());
    }

}
