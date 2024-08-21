package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerFormSearchCond extends CommonSearchCond {
    private Long userId;
    private Long bannerId;
}
