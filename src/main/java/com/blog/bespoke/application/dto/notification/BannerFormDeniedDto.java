package com.blog.bespoke.application.dto.notification;

import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerFormDeniedDto {
    BannerFormResponseDto bannerFormResponseDto;
}
