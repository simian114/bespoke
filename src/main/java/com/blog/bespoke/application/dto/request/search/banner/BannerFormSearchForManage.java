package com.blog.bespoke.application.dto.request.search.banner;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.domain.model.banner.BannerFormSearchCond;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerFormSearchForManage implements CommonSearchRequestDto<BannerFormSearchCond> {
    private Long bannerId;

    @Override
    public BannerFormSearchCond toModel() {
        BannerFormSearchCond cond = new BannerFormSearchCond();
        cond.setBannerId(bannerId);
        return cond;
    }
}
