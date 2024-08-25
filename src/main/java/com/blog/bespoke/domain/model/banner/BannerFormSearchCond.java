package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BannerFormSearchCond extends CommonSearchCond {
    private Long userId;
    private String nickname;

    private Long bannerId;

    private List<BannerFormStatus> statuses;

    private BannerUiType uiType;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
