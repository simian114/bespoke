package com.blog.bespoke.application.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossSuccessRequestDto {
    private String paymentKey;
    private String orderId;
    private Long amount;
    private String paymentType;
}
