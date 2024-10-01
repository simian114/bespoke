package com.blog.bespoke.application.dto.notification;

import com.blog.bespoke.domain.model.notification.ExtraInfo;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PublishNotificationDto implements NotificationDto {
    private Long publisherId;
    private Long postId;
    private String postTittle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    // TODO: recipient 를 추가해줘야함
    @Override
    public ExtraInfo getExtraInfo() {
        return ExtraInfo.builder()
                .postTitle(this.getPostTittle())
                .build();
    }

    // TODO: recipient 를 추가해줘야함
    @Override
    public Notification toModel() {
        return Notification.builder()
                .refId(this.getPostId())
                .publisher(User.builder().id(this.getPublisherId()).build())
                .extraInfo(this.getExtraInfo())
                .type(Notification.NotificationType.POST_PUBLISHED)
                .build();
    }

    public Notification toModel(User publisher, Long recipientId) {
        ExtraInfo extraInfo = this.getExtraInfo();
        extraInfo.setPublisher(publisher.getNickname());

        return Notification.builder()
                .refId(this.getPostId())
                .publisher(User.builder().id(this.getPublisherId()).build())
                .recipient(User.builder().id(recipientId).build())
                .extraInfo(this.getExtraInfo())
                .type(Notification.NotificationType.POST_PUBLISHED)
                .build();
    }

}
