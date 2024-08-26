package com.blog.bespoke.domain.model.payment;

// LINK: https://docs.tosspayments.com/reference#payment-%EA%B0%9D%EC%B2%B4

/**
 * 아래 토스 링크에서 상태 정보 확인.
 * https://docs.tosspayments.com/reference#payment-%EA%B0%9D%EC%B2%B4
 */
public enum PaymentStatus {
    NONE, // PAYMENT 를 처음 영구화했을 때는 NONE 상태를 가짐. 아무것도 아닌 상태.
    READY,
    IN_PROGRESS,
    WAITING_FOR_DEPOSIT,
    DONE,
    CANCELED,
    PARTIAL_CANCELED,
    ABORTED,
    EXPIRED
}
