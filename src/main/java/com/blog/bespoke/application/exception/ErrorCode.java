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
    USER_HAS_BLOCKED(HttpStatus.UNAUTHORIZED, "정지된 계정입니다."),
    USER_INACTIVE(HttpStatus.UNAUTHORIZED, "이메일 인증이 완료되지 않은 계정입니다."),
    USER_FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호를 확인해주세요."),

    // token
    TOKEN_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 토큰입니다."),

    // follow
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 정보가 없습니다."),

    // psot
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    BANNED_USER_POST(HttpStatus.BAD_REQUEST, "정지 된 유저의 게시글입니다."),
    POST_FORBIDDEN(HttpStatus.BAD_REQUEST, "접근 권한이 없는 게시글입니다.")
    ;

    private final HttpStatus statusCode;
    private final String message;
}
