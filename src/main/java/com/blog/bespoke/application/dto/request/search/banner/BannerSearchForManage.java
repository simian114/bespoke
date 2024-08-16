package com.blog.bespoke.application.dto.request.search.banner;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.domain.model.banner.BannerSearchCond;
import lombok.Setter;

@Setter
public class BannerSearchForManage implements CommonSearchRequestDto<BannerSearchCond> {
    private String nickname;

    @Override
    public BannerSearchCond toModel() {
        BannerSearchCond cond = new BannerSearchCond();
        cond.setAdvertiserNickname(nickname);
        return cond;
    }
}
