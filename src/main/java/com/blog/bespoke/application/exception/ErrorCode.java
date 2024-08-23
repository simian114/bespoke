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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Account does not exist."),
    USER_HAS_BLOCKED(HttpStatus.UNAUTHORIZED, "Account is suspended."),
    USER_INACTIVE(HttpStatus.UNAUTHORIZED, "Account is not email verified."),
    USER_FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "Please check your email or password."),
    EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "Email already exists."),
    EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "Nickname already exists."),

    NO_MORE_CATEGORY(HttpStatus.BAD_REQUEST, "You can create up to 3 categories."),
    DUPLICATE_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "Category names cannot be duplicated."),

    // token
    TOKEN_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "Token has expired."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Invalid token."),

    // follow
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "Follow information not found."),
    FOLLOWER_NOT_FOUND(HttpStatus.NOT_FOUND, "Follower information not found."),

    // post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post does not exist."),
    BANNED_USER_POST(HttpStatus.BAD_REQUEST, "Post by a suspended user."),
    POST_FORBIDDEN(HttpStatus.BAD_REQUEST, "You do not have permission to access this post."),
    POST_BAD_STATUS(HttpStatus.BAD_REQUEST, "Unable to modify the post."),

    PUBLISHED_POST_MUST_HAVE_CATEGORY(HttpStatus.BAD_REQUEST, "A category is required to publish a post."),

    // post like
    ALREADY_LIKE_POST(HttpStatus.BAD_REQUEST, "Post already liked."),
    POST_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Post not liked."),

    // image
    UNSUPPORTED_IMAGE(HttpStatus.BAD_REQUEST, "Only jpeg, jpg, png, gif, and webp formats are allowed."),
    OVER_AVATAR_LIMIT_SIZE(HttpStatus.BAD_REQUEST, "The maximum size for an avatar image is 1MB."),
    COVER_IMAGE_LIMIT_SIZE(HttpStatus.BAD_REQUEST, "The maximum size for a cover image is 5MB."),

    // banner
    BANNER_FORM_CANNOT_BE_AUDITED_STATUS(HttpStatus.BAD_REQUEST, "Only Pending status can be audited"),
    ;
    private final HttpStatus statusCode;
    private final String message;
}
