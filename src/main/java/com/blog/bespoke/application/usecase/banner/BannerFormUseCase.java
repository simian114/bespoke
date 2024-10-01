package com.blog.bespoke.application.usecase.banner;

import com.blog.bespoke.application.dto.request.BannerFormCreateRequestDto;
import com.blog.bespoke.application.dto.request.EstimatedPaymentRequestDto;
import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.application.event.message.BannerAuditApproveMessage;
import com.blog.bespoke.application.event.message.BannerAuditDenyMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.domain.model.banner.*;
import com.blog.bespoke.domain.model.payment.Payment;
import com.blog.bespoke.domain.model.payment.PaymentRefType;
import com.blog.bespoke.domain.model.payment.PaymentStatus;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.banner.BannerFormRepository;
import com.blog.bespoke.domain.repository.banner.BannerRepository;
import com.blog.bespoke.domain.repository.payment.PaymentRepository;
import com.blog.bespoke.domain.service.banner.BannerFormService;
import com.blog.bespoke.domain.service.banner.BannerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BannerFormUseCase {
    private static final Logger log = LoggerFactory.getLogger(BannerFormUseCase.class);
    private final BannerService bannerService;
    private final BannerFormService bannerFormService;
    private final BannerRepository bannerRepository;
    private final BannerFormRepository bannerFormRepository;
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    @Transactional
    public BannerFormResponseDto getBannerFormById(Long bannerFormId) {
        BannerForm bannerForm = bannerFormRepository.getById(bannerFormId);
        return BannerFormResponseDto.from(bannerForm);
    }

    @Transactional
    public BannerFormResponseDto createBannerForm(BannerFormCreateRequestDto requestDto, Long bannerId, User currentUser) {
        // TODO: banner and save as string
        Banner banner = bannerRepository.getById(bannerId);

        BannerForm bannerForm = requestDto.toModel(banner, currentUser);
        BannerForm savedBannerForm = bannerFormRepository.save(bannerForm);
        BannerForm foundBannerForm = bannerFormRepository.getById(savedBannerForm.getId());
        return BannerFormResponseDto.from(foundBannerForm);
    }

    @Transactional
    public BannerFormResponseDto audit(Long bannerId, boolean isApprove, String denyReason) {
        // TODO: preload user 로직 구현하기
        BannerFormRelation relation = BannerFormRelation.builder().user(true).build();
        // NOTE: user image 바로 가져오도록 수정
        BannerForm bannerForm = bannerFormRepository.getById(bannerId, relation);

        // exception 날림
        bannerFormService.checkCanBeAuditedBannerForm(bannerForm);

        if (isApprove) {
            bannerForm.approve();
        } else {
            bannerForm.deny(denyReason);
        }
        BannerForm updatedBannerForm = bannerFormRepository.save(bannerForm);
        BannerFormResponseDto responseDto = BannerFormResponseDto.from(updatedBannerForm, relation);

        // TODO: payment 객체 생성
        Payment payment = Payment.builder()
                .refId(bannerForm.getId())
                .refType(PaymentRefType.BANNER_FORM)
                .amount(bannerService.calculatePrice(bannerForm))
                .user(User.builder().id(bannerForm.getUser().getId()).build())
                .orderId(UUID.randomUUID().toString())
                .orderName("배너 구입") // 이름 변경하기
                .status(PaymentStatus.NONE)
                .build();

        paymentRepository.save(payment);

        // event
        String bannerFormAsString = "";
        try {
            bannerFormAsString = objectMapper.writeValueAsString(responseDto);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        if (isApprove) {
            // TODO: extra 설정을 안해줌
            eventPublisher.publishBannerAuditApproveEvent(
                    BannerAuditApproveMessage.builder()
                            .bannerFormResponseDtoAsString(bannerFormAsString)
                            .build());
        } else {
            // TODO: extra 설정을 안해줌
            eventPublisher.publishBannerAuditDenyEvent(
                    BannerAuditDenyMessage.builder()
                            .bannerFormResponseDtoAsString(bannerFormAsString)
                            .build());
        }
        return responseDto;
    }

    @Transactional
    public BannerFormResponseDto pay(Long bannerFormId) {
        BannerForm bannerForm = bannerFormRepository.getById(bannerFormId);
        bannerForm.pay();
        return BannerFormResponseDto.from(bannerForm);
    }

    public Long calculatedEstimatedPayment(EstimatedPaymentRequestDto requestDto, User currentUser) {
        return bannerService.calculatePrice(requestDto.getUiType(), requestDto.getDuration(), currentUser);
    }

    public BannerFormResponseDto getOnGoingBannerForm(List<BannerFormResponseDto> bannerForms) {
        return bannerForms.stream().filter(form -> {
            BannerForm bannerForm = BannerForm.builder().status(form.getStatus()).build();
            return bannerForm.isOngoing();
        }).findFirst().orElse(null);
    }

    /**
     * 결제가 완료된 상태면서 start date 된 banner 를 실제 게시하는 메서드
     * 스케줄러 / 수동 실행 등 다양한 곳에서 사용될 수 있어야함.
     */
    @Transactional
    public void publishPaymentCompleted() {
        BannerFormSearchCond cond = new BannerFormSearchCond();
        cond.setStatuses(List.of(BannerFormStatus.PAYMENT_COMPLETED));
        cond.setPageSize(100);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 1, 23, 59, 59);
        cond.setStartDate(startOfToday);
        Page<BannerForm> searchRes = bannerFormRepository.search(cond);
        List<BannerForm> bannerForms = searchRes.getContent();
        bannerForms.forEach(BannerForm::publish);
        System.out.println("publish banner");
        bannerFormRepository.saveAll(bannerForms);
        // TODO: banner Redis 초기화해야함
    }

    /**
     * 기간 종료 된 배너 내리기
     */
    @Transactional
    public void exitEndedBanner() {
        BannerFormSearchCond cond = new BannerFormSearchCond();

        cond.setStatuses(List.of(BannerFormStatus.PUBLISHED));
        cond.setPageSize(100);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
        cond.setEndDate(endDate);

        Page<BannerForm> searchRes = bannerFormRepository.search(cond);
        List<BannerForm> bannerForms = searchRes.getContent();
        bannerForms.forEach(BannerForm::end);
        bannerFormRepository.saveAll(bannerForms);
        // TODO: banner Redis 초기화해야함
    }
}
