package com.blog.bespoke.application.usecase.banner;

import com.blog.bespoke.application.dto.request.BannerFormCreateRequestDto;
import com.blog.bespoke.application.dto.request.EstimatedPaymentRequestDto;
import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.banner.BannerFormRepository;
import com.blog.bespoke.domain.repository.banner.BannerRepository;
import com.blog.bespoke.domain.service.banner.BannerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerFormUseCase {
    private final BannerService bannerService;
    private final BannerRepository bannerRepository;
    private final BannerFormRepository bannerFormRepository;

    @Transactional
    public BannerFormResponseDto createBannerForm(BannerFormCreateRequestDto requestDto, Long bannerId, User currentUser) {
        // TODO: banner and save as string
        Banner banner = bannerRepository.getById(bannerId);

        BannerForm bannerForm = requestDto.toModel(banner, currentUser);
        BannerForm savedBannerForm = bannerFormRepository.save(bannerForm);
        BannerForm foundBannerForm = bannerFormRepository.getById(savedBannerForm.getId());
        return BannerFormResponseDto.from(foundBannerForm);
    }

    public Long calculatedEstimatedPayment(EstimatedPaymentRequestDto requestDto, User currentUser) {
        return bannerService.calculateAmount(requestDto.getUiType(), requestDto.getDuration(), currentUser);
    }

    public BannerFormResponseDto getOnGoingBannerForm(List<BannerFormResponseDto> bannerForms) {
        return bannerForms.stream().filter(form -> {
            BannerForm bannerForm = BannerForm.builder().status(form.getStatus()).build();
            return bannerForm.isOngoing();
        }).findFirst().orElse(null);
    }
}
