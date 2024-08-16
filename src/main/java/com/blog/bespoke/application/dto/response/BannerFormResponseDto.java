package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormStatus;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@Builder
public class BannerFormResponseDto {
    private Long id;
    private BannerFormStatus status;
    private String startDate;
    private String endDate;

    private BannerResponseDto bannerSnapshot;

    private String createdAt;
    private String auditedAt;

    static public BannerFormResponseDto from(BannerForm bannerForm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return BannerFormResponseDto.builder()
                .id(bannerForm.getId())
                .status(bannerForm.getStatus())
                .startDate(bannerForm.getStartDate().format(formatter))
                .endDate(bannerForm.getEndDate().format(formatter))
                .bannerSnapshot(BannerResponseDto.from(bannerForm.getBannerObj()))
                .createdAt(bannerForm.getCreatedAt().format(formatter))
                .auditedAt(bannerForm.getAuditedAt().format(formatter))
                .build();
    }
}
