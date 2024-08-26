package com.blog.bespoke.domain.service.banner;

import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerUiType;
import com.blog.bespoke.domain.model.user.User;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class BannerService {
    private final Long MAIN_HERO_DAILY_PRICE = 10000L;
    private final Long MAIN_POPUP_DAILY_PRICE = 10000L;
    private final Long TOP_BAR_DAILY_PRICE = 10000L;

    /**
     * 타입에 따라 가격이 다르게 측정됨.
     * 어드민은 무료
     */
    public Long calculatePrice(BannerUiType type, Integer duration, User currentUser) {
        if (currentUser.isAdmin()) {
            return 0L;
        }
        return priceOfDayByType(type) * duration;

    }

    public Long calculatePrice(BannerForm bannerForm) {
        return ChronoUnit.DAYS.between(bannerForm.getStartDate(), bannerForm.getEndDate());
    }


    private Long priceOfDayByType(BannerUiType type) {
        switch (type) {
            case MAIN_HERO  -> {
                return MAIN_HERO_DAILY_PRICE;
            }
            case MAIN_POPUP -> {
                return  MAIN_POPUP_DAILY_PRICE;
            }
            case TOP_BAR -> {
                return TOP_BAR_DAILY_PRICE;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
