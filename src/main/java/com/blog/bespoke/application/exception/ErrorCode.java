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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."),
    USER_HAS_BLOCKED(HttpStatus.UNAUTHORIZED, "정지된 계정입니다."),
    USER_INACTIVE(HttpStatus.UNAUTHORIZED, "이메일 인증이 완료되지 않은 계정입니다."),
    USER_FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호를 확인해주세요."),
    EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

    NO_MORE_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리는 최대 3개까지 생성 가능합니다."),
    DUPLICATE_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "카테고리 이름은 중복될 수 없습니다."),

    // token
    TOKEN_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 토큰입니다."),

    // follow
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 정보가 없습니다."),
    FOLLOWER_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로워 정보가 없습니다."),

    // post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    BANNED_USER_POST(HttpStatus.BAD_REQUEST, "정지 된 유저의 게시글입니다."),
    POST_FORBIDDEN(HttpStatus.BAD_REQUEST, "접근 권한이 없는 게시글입니다."),
    POST_BAD_STATUS(HttpStatus.BAD_REQUEST, "수정 할 수 없습니다."),

    PUBLISHED_POST_MUST_HAVE_CATEGORY(HttpStatus.BAD_REQUEST, "게시되기 위해서는 반드시 카테고리가 있어야합니다."),

    // post like
    ALREADY_LIKE_POST(HttpStatus.BAD_REQUEST, "이미 좋아요한 게시글입니다."),
    POST_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "좋아요되지 않은 게시글입니다."),

    // image
    UNSUPPORTED_IMAGE(HttpStatus.BAD_REQUEST, "jpeg, jpg, png, gif, webp 만 올릴 수 있습니다." ),
    OVER_AVATAR_LIMIT_SIZE(HttpStatus.BAD_REQUEST, "아바타 이미지 최대 크기는 1MB입니다.")
    ;

    private final HttpStatus statusCode;
    private final String message;
}
