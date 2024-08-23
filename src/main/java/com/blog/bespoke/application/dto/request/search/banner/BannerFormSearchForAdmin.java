package com.blog.bespoke.application.dto.request.search.banner;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.domain.model.banner.BannerFormSearchCond;
import com.blog.bespoke.domain.model.banner.BannerFormStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BannerFormSearchForAdmin implements CommonSearchRequestDto<BannerFormSearchCond> {
    private String nickname;

    private Long bannerId;

    private List<BannerFormStatus> statuses;

    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public BannerFormSearchCond toModel() {
        BannerFormSearchCond cond = new BannerFormSearchCond();
        cond.setBannerId(bannerId);
        cond.setNickname(nickname);


//        startDate.atTime(23,59,59).minusDays(1)
        cond.setStartDate(startDate != null ? startDate.atTime(23, 59, 59).minusDays(1) : null);
//        cond.setStartDate(startDate != null ? startDate.atStartOfDay() : null);
        cond.setEndDate(endDate != null ? endDate.atTime(23, 59, 59) : null);

        cond.setStatuses(statuses);
        return cond;
    }
}
