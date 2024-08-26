package com.blog.bespoke.domain.repository.payment;

import com.blog.bespoke.domain.model.payment.Payment;
import com.blog.bespoke.domain.model.payment.PaymentSearchCond;
import org.springframework.data.domain.Page;

public interface PaymentSearchRepository {
    Page<Payment> search(PaymentSearchCond cond);
}
