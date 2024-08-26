package com.blog.bespoke.application.usecase.payment;

import com.blog.bespoke.application.dto.request.payment.TossSuccessRequestDto;
import com.blog.bespoke.application.dto.response.PaymentResponseDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.payment.Payment;
import com.blog.bespoke.domain.model.payment.PaymentRefType;
import com.blog.bespoke.domain.model.payment.PaymentReferenceTypeObject;
import com.blog.bespoke.domain.repository.banner.BannerFormRepository;
import com.blog.bespoke.domain.repository.banner.BannerRepository;
import com.blog.bespoke.domain.repository.payment.PaymentRepository;
import com.blog.bespoke.domain.service.banner.BannerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentUseCase {
    private final PaymentRepository paymentRepository;
    private final BannerRepository bannerRepository;
    private final BannerFormRepository bannerFormRepository;
    private final BannerService bannerService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public PaymentResponseDto getPaymentByRef(Long refId, PaymentRefType refType) {
        Payment payment = paymentRepository.getByRefIdAndRefType(refId, refType);
        return PaymentResponseDto.from(payment);
    }

    @Transactional
    public PaymentResponseDto tossPayConfirm(Long paymentId, TossSuccessRequestDto requestDto) {
        Payment payment = paymentRepository.getById(paymentId);

        // TODO: 환경변수로 변경
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", authorizations);
        headers.add("Content-Type", "application/json");

        HttpEntity<TossSuccessRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<Payment> resEntity = restTemplate.postForEntity("https://api.tosspayments.com/v1/payments/confirm", entity, Payment.class);
        Payment updatedPayment = resEntity.getBody();

        HttpStatusCode statusCode = resEntity.getStatusCode();
        // TODO: success
        if (statusCode.is2xxSuccessful() && updatedPayment != null) {
            //
            payment.confirmed(updatedPayment);
            paymentCompleteForRefObject(payment);
        } else {
            throw new BusinessException(ErrorCode.TOSS_PAYMENT_FAILED);
        }
        return PaymentResponseDto.from(payment);
    }

    @Transactional
    public void paymentCompleteForRefObject(Payment payment) {
        PaymentReferenceTypeObject refObject = getPaymentReferenceTypeObject(payment);
        refObject.paymentCompleted();
    }

    @Transactional
    public PaymentReferenceTypeObject getPaymentReferenceTypeObject(Payment payment) {
        PaymentRefType refType = payment.getRefType();
        PaymentReferenceTypeObject refObject = null;
        if (refType.equals(PaymentRefType.BANNER_FORM)) {
            refObject = bannerFormRepository.getById(payment.getRefId());
        }
        return refObject;
    }


}
