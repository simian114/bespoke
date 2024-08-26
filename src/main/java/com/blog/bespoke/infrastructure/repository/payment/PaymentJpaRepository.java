package com.blog.bespoke.infrastructure.repository.payment;

import com.blog.bespoke.domain.model.payment.Payment;
import com.blog.bespoke.domain.model.payment.PaymentRefType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRefIdAndRefType(Long refId, PaymentRefType refType);

    Optional<Payment> findByOrderId(String orderId);
}
