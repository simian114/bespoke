package com.blog.bespoke.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException{
    private final HttpStatus statusCode;
    private final String message;

    public BusinessException(ErrorCode errorCode) {
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
    }
}
