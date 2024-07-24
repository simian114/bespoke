package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.notification.Notification;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class NotificationResponseDto {
    private Long id;
    private boolean readed;
    private String content; // extraInfo 로 조합되는 문자열

    private String publisher; // publisher nickname
    private String recipient; // recipient nickname

    private LocalDateTime createdAt;

    //    private String type;
    private Notification.NotificationType type;
    private Long refId;

    static public NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .refId(notification.getRefId())
                .readed(notification.isReaded())
                .type(notification.getType())
                .publisher(notification.getExtraInfo().getPublisher())
                .recipient(notification.getExtraInfo().getRecipient())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
