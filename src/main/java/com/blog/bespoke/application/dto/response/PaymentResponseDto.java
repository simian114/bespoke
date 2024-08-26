package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.payment.Payment;
import com.blog.bespoke.domain.model.payment.PaymentRefType;
import com.blog.bespoke.domain.model.payment.PaymentRelation;
import com.blog.bespoke.domain.model.user.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private Long refId;
    private PaymentRefType refType;
    private Long amount;
    private String orderName;
    private String orderId;
    private User user;

    static private PaymentResponseDtoBuilder base(Payment payment) {
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .refId(payment.getRefId())
                .refType(payment.getRefType())
                .orderName(payment.getOrderName())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount());
    }

    static public PaymentResponseDto from(Payment payment) {
        return base(payment)
                .build();
    }

    static public PaymentResponseDto from(Payment payment, PaymentRelation relation) {
        return base(payment)
                .user(relation.isUser() ? payment.getUser() : null)
                .build();
    }


}
