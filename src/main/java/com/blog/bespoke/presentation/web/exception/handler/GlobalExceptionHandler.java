package com.blog.bespoke.presentation.web.exception.handler;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.ResponseEnvelope;
import com.blog.bespoke.presentation.web.exception.BasicErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ResponseEnvelope<BasicErrorResponse>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatusCode().value())
                .body(
                        new ResponseEnvelope<>(
                                BasicErrorResponse.builder()
                                        .timestamp(LocalDateTime.now())
                                        .error(e.getStatusCode().getReasonPhrase())
                                        .path(request.getRequestURI())
                                        .build(),
                                e.getMessage(),
                                e.getStatusCode().value()
                        )
                );
    }

    @ExceptionHandler
    public ResponseEntity<?> handleBeanValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    FieldError fieldError = (FieldError) error;
                    return String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage());
                })
                .collect(Collectors.joining("\n"));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ResponseEnvelope(
                                BasicErrorResponse.builder()
                                        .timestamp(LocalDateTime.now())
                                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                        .path(request.getRequestURI())
                                        .build(),
                                errorMessage,
                                HttpStatus.BAD_REQUEST.value()
                        ));
    }
}
