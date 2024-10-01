package com.blog.bespoke.application.dto.notification;

import com.blog.bespoke.domain.model.notification.ExtraInfo;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostLikeNotificationDto implements NotificationDto {
    private Long userId;
    private String userNickname;
    private Long authorId;
    private String authorNickname;
    private Long postId;
    private String postTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Override
    public ExtraInfo getExtraInfo() {
        return ExtraInfo.builder()
                .recipient(this.getAuthorNickname())
                .postTitle(this.getPostTitle())
                .publisher(this.getUserNickname())
                .build();
    }

    @Override
    public Notification toModel() {
        return Notification.builder()
                .type(Notification.NotificationType.POST_LIKE)
                .refId(this.getPostId())
                .extraInfo(this.getExtraInfo())
                .recipient(User.builder().id(this.getAuthorId()).build())
                .publisher(User.builder().id(this.getUserId()).build())
                .build();
    }
}
