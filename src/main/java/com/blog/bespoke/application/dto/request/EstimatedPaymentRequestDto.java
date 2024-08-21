package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.banner.BannerUiType;
import lombok.Data;

@Data
public class EstimatedPaymentRequestDto {
    private Integer duration;
    private BannerUiType uiType;
}
