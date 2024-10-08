package com.blog.bespoke.application.dto.notification;

import com.blog.bespoke.domain.model.notification.ExtraInfo;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowNotificationDto implements NotificationDto {
    private Long publisherId;
    private Long recipientId;
    private String publisherNickname;
    private String recipientNickname;

    @Override
    public ExtraInfo getExtraInfo() {
        return ExtraInfo.builder()
                .recipient(this.getRecipientNickname())
                .publisher(this.getPublisherNickname())
                .build();
    }

    @Override
    public Notification toModel() {
        return Notification.builder()
                .type(Notification.NotificationType.FOLLOW)
                .refId(null)
                .extraInfo(this.getExtraInfo())
                .recipient(User.builder().id(this.getRecipientId()).build())
                .publisher(User.builder().id(this.getPublisherId()).build())
                .build();
    }

}

