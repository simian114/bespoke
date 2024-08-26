package com.blog.bespoke.application.event.message;

import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.payment.PaymentRefType;
import com.blog.bespoke.domain.model.token.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BannerAuditApproveMessage {
    private String bannerFormResponseDtoAsString;
}
