package com.blog.bespoke.application.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostLikeNotificationDto {
    private Long userId;
    private String userNickname;
    private Long authorId;
    private String authorNickname;
    private Long postId;
    private String postTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
