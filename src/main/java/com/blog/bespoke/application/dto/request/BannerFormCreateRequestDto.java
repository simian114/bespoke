package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormStatus;
import com.blog.bespoke.domain.model.user.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BannerFormCreateRequestDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private Integer duration;

    public BannerForm toModel(Banner banner, User currentUser) {
        LocalDateTime s = LocalDateTime.from(startDate.atStartOfDay());
        LocalDate endDate = startDate.plusDays(duration - 1);
        LocalDateTime e = endDate.atTime(23, 59, 59);
        return BannerForm.builder()
                .startDate(s)
                .endDate(e)
                .status(BannerFormStatus.PENDING)
                .banner(banner)
                .user(currentUser)
                .build();
    }
}
