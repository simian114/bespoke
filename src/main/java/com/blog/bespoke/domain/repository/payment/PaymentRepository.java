package com.blog.bespoke.domain.repository.payment;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.domain.model.payment.Payment;
import com.blog.bespoke.domain.model.payment.PaymentRefType;

import java.util.Optional;

public interface PaymentRepository extends PaymentSearchRepository {
    Payment save(Payment payment);

    Optional<Payment> findById(Long paymentId);

    Payment getById(Long paymentId) throws BusinessException;

    Optional<Payment> findByOrderId(String orderId);

    Payment getByOrderId(String orderId);

    void delete(Payment payment);

    void deleteById(Long paymentId);

    Optional<Payment> findByRefIdAndRefType(Long refId, PaymentRefType refType);

    Payment getByRefIdAndRefType(Long refId, PaymentRefType refType);


}
