package com.blog.bespoke.application.usecase.banner;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.BannerSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.banner.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerSearchUseCase {
    private final BannerRepository bannerRepository;

    public CommonSearchResponseDto<BannerResponseDto> searchBanner(CommonSearchRequestDto<BannerSearchCond> requestDto, User currentUser) {
        BannerSearchCond cond = requestDto.toModel();

        // currentUser 와 cond 조건 따지기..
        Page<Banner> res = bannerRepository.search(cond);

        Page<BannerResponseDto> map = res.map(BannerResponseDto::from);
        return (new CommonSearchResponseDto<BannerResponseDto>()).from(map);
    }

}
