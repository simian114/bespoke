package com.blog.bespoke.application.dto.notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowNotificationDto {
    private Long publisherId;
    private Long recipientId;
    private String publisherNickname;
    private String recipientNickname;
}

