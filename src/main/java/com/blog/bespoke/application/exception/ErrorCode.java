package com.blog.bespoke.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // basic
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),

    // user
    USER_HAS_BLOCKED(HttpStatus.UNAUTHORIZED, "BLOCK USER"),
    USER_INACTIVE(HttpStatus.UNAUTHORIZED, "INACTIVE USER"),
    USER_FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "username 또는 password 가 올바르지 않습니다")
    ;

    private final HttpStatus statusCode;
    private final String message;
}
