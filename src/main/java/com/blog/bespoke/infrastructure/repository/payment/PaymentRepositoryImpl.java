package com.blog.bespoke.infrastructure.repository.payment;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormSearchCond;
import com.blog.bespoke.domain.model.banner.QBannerForm;
import com.blog.bespoke.domain.model.payment.Payment;
import com.blog.bespoke.domain.model.payment.PaymentRefType;
import com.blog.bespoke.domain.model.payment.PaymentSearchCond;
import com.blog.bespoke.domain.model.payment.QPayment;
import com.blog.bespoke.domain.repository.payment.PaymentRepository;
import com.blog.bespoke.domain.repository.payment.PaymentSearchRepository;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final JPAQueryFactory queryFactory;
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long paymentId) {
        return paymentJpaRepository.findById(paymentId);
    }

    @Override
    public Payment getById(Long paymentId) throws BusinessException {
        return findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        return paymentJpaRepository.findByOrderId(orderId);
    }

    @Override
    public Payment getByOrderId(String orderId) {
        return findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    @Override
    public Optional<Payment> findByRefIdAndRefType(Long refId, PaymentRefType refType) {
        return paymentJpaRepository.findByRefIdAndRefType(refId, refType);
    }

    @Override
    public Payment getByRefIdAndRefType(Long refId, PaymentRefType refType) {
        return findByRefIdAndRefType(refId, refType)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    @Override
    public void delete(Payment payment) {
        paymentJpaRepository.delete(payment);
    }

    @Override
    public void deleteById(Long paymentId) {
        paymentJpaRepository.deleteById(paymentId);
    }

        @Override
    public Page<Payment> search(PaymentSearchCond cond) {
        Pageable pageable = cond.getPageable();

        JPAQuery<Payment> jpaQuery = query(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        jpaQuery.orderBy(QPayment.payment.createdAt.desc());
        List<Payment> payments = jpaQuery.fetch();

        Long totalSize = cond.isCount() ? countQuery(cond).fetch().get(0) : 0L;

        return PageableExecutionUtils.getPage(payments, pageable, () -> totalSize);
    }

    private JPAQuery<Payment> query(PaymentSearchCond cond) {
        return queryFactory.select(QPayment.payment)
                .from(QPayment.payment)
                // bannerForm 은 굳이 join 할 필요 없음
                .where(
//                        userIdEq(cond),
                );
    }

    private JPAQuery<Long> countQuery(PaymentSearchCond cond) {
        return queryFactory.select(Wildcard.count)
                .from(QPayment.payment)
                .where(
//                        userIdEq(cond),
                );
    }


    //
}
