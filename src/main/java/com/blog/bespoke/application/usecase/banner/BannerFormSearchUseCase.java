package com.blog.bespoke.application.usecase.banner;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormSearchCond;
import com.blog.bespoke.domain.model.banner.BannerFormStatus;
import com.blog.bespoke.domain.model.banner.BannerUiType;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.banner.BannerFormRepository;
import com.blog.bespoke.domain.repository.banner.BannerFormSearchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerFormSearchUseCase {
    private final BannerFormSearchRepository bannerFormSearchRepository;
    private final BannerFormRepository bannerFormRepository;

    @Transactional
    public CommonSearchResponseDto<BannerFormResponseDto> searchBannerForm(CommonSearchRequestDto<BannerFormSearchCond> requestDto, User currentUser) {
        BannerFormSearchCond cond = requestDto.toModel();
        Page<BannerForm> res = bannerFormRepository.search(cond);
        Page<BannerFormResponseDto> map = res.map(BannerFormResponseDto::from);
        return (new CommonSearchResponseDto<BannerFormResponseDto>()).from(map);
    }

    @Transactional
    public CommonSearchResponseDto<BannerFormResponseDto> getTopBanners(Integer pageSize) {
        BannerFormSearchCond cond = new BannerFormSearchCond();
        cond.setStatuses(List.of(BannerFormStatus.PUBLISHED));
        cond.setUiType(BannerUiType.TOP_BAR);
        cond.setCount(false);
        cond.setPage(0);
        cond.setPageSize(3);
        Page<BannerForm> res = bannerFormRepository.search(cond);
        Page<BannerFormResponseDto> map = res.map(BannerFormResponseDto::from);
        return (new CommonSearchResponseDto<BannerFormResponseDto>()).from(map);
    }

    /**
     * main 홈 페이지 배너
     * - 히어로
     * - 팝업
     * 캐싱이 반드시 필요하다. 캐싱이 안되면 서비스 자체가 느려질게 분명함
     */
    @Transactional
    public List<BannerResponseDto> getMainHeroBanner() {
        BannerFormSearchCond cond = new BannerFormSearchCond();
        cond.setStatuses(List.of(BannerFormStatus.PUBLISHED));
        cond.setUiType(BannerUiType.MAIN_HERO);
        cond.setCount(false);
        cond.setPage(0);
        cond.setPageSize(5);
        Page<BannerForm> res = bannerFormRepository.search(cond);
        return res.map(bf -> BannerFormResponseDto.from(bf).getBannerSnapshot())
                .getContent();
    }

    @Transactional
    public List<BannerResponseDto> getMainPopupBanner() {
        BannerFormSearchCond cond = new BannerFormSearchCond();
        cond.setStatuses(List.of(BannerFormStatus.PUBLISHED));
        cond.setUiType(BannerUiType.MAIN_POPUP);
        cond.setPage(0);
        cond.setCount(false);
        cond.setPageSize(5);
        Page<BannerForm> res = bannerFormRepository.search(cond);
        return res.map(bf -> BannerFormResponseDto.from(bf).getBannerSnapshot())
                .getContent();
    }


}
