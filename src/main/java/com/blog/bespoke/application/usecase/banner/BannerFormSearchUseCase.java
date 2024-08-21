package com.blog.bespoke.application.usecase.banner;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.banner.BannerFormRepository;
import com.blog.bespoke.domain.repository.banner.BannerFormSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerFormSearchUseCase {
    private final BannerFormSearchRepository bannerFormSearchRepository;
    private final BannerFormRepository bannerFormRepository;

    public CommonSearchResponseDto<BannerFormResponseDto> searchBannerForm(CommonSearchRequestDto<BannerFormSearchCond> requestDto, User currentUser) {
        BannerFormSearchCond cond = requestDto.toModel();
        Page<BannerForm> res = bannerFormRepository.search(cond);
        Page<BannerFormResponseDto> map = res.map(BannerFormResponseDto::from);
        return (new CommonSearchResponseDto<BannerFormResponseDto>()).from(map);
    }

}
