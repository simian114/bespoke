package com.blog.bespoke.application.dto.notification;

import com.blog.bespoke.domain.model.notification.ExtraInfo;
import com.blog.bespoke.domain.model.notification.Notification;

public interface NotificationDto {
    ExtraInfo getExtraInfo();

    Notification toModel();
}
