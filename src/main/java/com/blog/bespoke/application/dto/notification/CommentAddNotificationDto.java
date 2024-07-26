package com.blog.bespoke.application.dto.notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentAddNotificationDto {
    private Long publisherId;
    private Long recipientId;

    private String publisherNickname;
    private String recipientNickname;

    private Long postId;
    private String postTitle;

    private Long commentId;
    private String commentContent;
}
