package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;

@Getter
public class BannerFormSearchCond extends CommonSearchCond {
    private Long userId;
}
