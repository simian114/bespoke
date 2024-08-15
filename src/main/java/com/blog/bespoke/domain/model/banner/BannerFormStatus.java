package com.blog.bespoke.domain.model.banner;

/**
 * Banner 가 필요없어 보인다.
 * 왜? status 가 현재 PENDING / DENIED / APPROVED 가 있는데,
 * APPROVED 를 없애고, 결제대기중, 결제취소, 결제 완료, 게시중, 게시종료, 환불 요청, 환불됨
 * 와 같은 상태를 더 추가하면 될거같다. 각각의 상태를 구체적으로 정리해보면
 * - 결제대기중(PAYMENT_WAITING): 어드민이 승인을 하면 자동으로 결제 대기중 상태로 들어간다. 말 그대로 결제 대기중인 상태다.
 * - 결제취소(PAYMENT_CANCEL): 결제 대기 중 상태에서 취소 된 상태다. 원상태로의 복구가 불가능하다. 처음부터 심사를 다시 받아야한다.
 * - 결제완료(PAYMENT_COMPLETED): 결제가 완료 되고, 광고가 게시되기 전까지 대가히는 상태다
 * - 결제 완료 상태면 100% 환불을 받을 수 있어야한다.
 * - 환불 요청(REFUND_REQUEST): 환불을 요청한 상태다.
 * - 부분환불 / 전액환불
 * - 결제완료 상태에서 환불 요청 전액 환불이 되어야한다.
 * - 게시중에 환불 요청을 하면 그 즉시 광고가 내려간다. 환불은 해당날의 종료 시간을 기준으로 % 만큼 환불됨
 * - 환불됨(REFUND_COMPLETED): 환불이 완료된 상태다.
 * - 광고 일시 정지(SUSPENDED): 광고가 어떤 문제 / 광고주의 요청에 의해 일시 정지 된 상태다.
 * - 광고 중지(TERMINATED): 광고 완전 중지. 심각한 문제로 광고를 아예 중지한다.
 * - 게시중(PUBLISHED): 결제완료 단계에서 광고 기간에 도달하면, 스케줄러에 의해 자동으로 게시중으로 변경된다.
 * - 게시종료(END): 게시중인 광고가 스케줄러에 의해 자동으로 게시종료가 된다.
 *
 *
 * 완전 종료 상태는 아래와 같다.
 *   - DENIED
 *   - REFUND_COMPLETED
 *   - TERMINATED
 *   - END
 */
public enum BannerFormStatus {
    PENDING, // 심사 대기중
    DENIED, // 심사 거절
    PAYMENT_WAITING, // 심사 승인 후, 결제 대기 중
    PAYMENT_CANCEL, // 결제 취소
    PAYMENT_COMPLETED,  // 결제 완료
    REFUND_REQUEST, // 환불 요청
    REFUND_COMPLETED, // 환불 완료
    SUSPENDED, // 일시 중지
    TERMINATED, // 영구 중지
    PUBLISHED, // 게시중
    END // 게시 종료
}
