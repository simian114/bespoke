package com.blog.bespoke.application.dto.notification;

import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.domain.model.notification.ExtraInfo;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerFormApprovedNotificationDto implements NotificationDto {
    BannerFormResponseDto bannerFormResponseDto;

    @Override
    public ExtraInfo getExtraInfo() {
        return ExtraInfo.builder()
                .recipient(this.bannerFormResponseDto.getUser().getNickname())
                .build();
    }

    @Override
    public Notification toModel() {
        return Notification.builder()
                .type(Notification.NotificationType.BANNER_FORM_APPROVED)
                .refId(this.getBannerFormResponseDto().getId())
                .recipient(User.builder().id(this.getBannerFormResponseDto().getUser().getId()).build())
                .publisher(null)
                .extraInfo(this.getExtraInfo())
                .build();
    }
}
