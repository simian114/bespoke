package com.blog.bespoke.domain.model.payment;

import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * TossPayment
 */
@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Long refId;

    @Enumerated(EnumType.STRING)
    private PaymentRefType refType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // ----- TOSS PAYMENTS START
    // LINK: https://docs.tosspayments.com/reference#payment-%EA%B0%9D%EC%B2%B4
    private String paymentKey;
    @Enumerated(EnumType.STRING)
    private PaymentType type; // NORMAL / BILLING / BRANDPAY
    private String orderId;
    private String orderName;
    private String mId;
    private String method;
//    private LocalDateTime requestedAt;
//    private LocalDateTime approvedAt;
    private Long vat;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Long amount;

    // NOTE: 많은 정보가 생략됨. 그 정보들은 토스페이먼츠 어드민 페이지를 통해 접근할 수 있다.
    // TODO: 그 링크 달아놓기

    // ----- TOSS PAYMENTS END

    public void confirmed(Payment updatedPayment) {
        status = updatedPayment.getStatus();
    }
}
