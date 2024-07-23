package com.blog.bespoke.domain.model.notification;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationSearchCond extends CommonSearchCond {
    //
    private Boolean readed; // null 전체, true: 읽은것만, false: 읽지 않은
    private Notification.NotificationType type; // null 이면 전체
    private Long recipientId;
}
