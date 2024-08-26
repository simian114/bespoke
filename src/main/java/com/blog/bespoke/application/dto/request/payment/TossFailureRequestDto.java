package com.blog.bespoke.application.dto.request.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TossFailureRequestDto {
    private String code;
    private String message;
    private String orderId;
}
