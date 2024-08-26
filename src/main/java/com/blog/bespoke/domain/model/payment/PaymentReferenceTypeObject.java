package com.blog.bespoke.domain.model.payment;

public interface PaymentReferenceTypeObject {
    PaymentRefType getPaymentRefType();

    void paymentCompleted();

    void paymentConfirmFailed();
}
