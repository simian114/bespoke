package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.banner.*;
import com.blog.bespoke.domain.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerFormResponseDto {
    private Long id;
    private BannerFormStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private BannerUiType uiType;

    private BannerResponseDto bannerSnapshot;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    private LocalDateTime auditedAt;

    private UserResponseDto user;

    static private BannerFormResponseDtoBuilder base(BannerForm bannerForm) {
        if (!bannerForm.getBannerSnapshot().isBlank() && bannerForm.getBannerObj() == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                bannerForm.setBannerObj(objectMapper.readValue(bannerForm.getBannerSnapshot(), Banner.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return BannerFormResponseDto.builder()
                .id(bannerForm.getId())
                .status(bannerForm.getStatus())
                .startDate(bannerForm.getStartDate())
                .endDate(bannerForm.getEndDate())
                .uiType(bannerForm.getUiType())
                .bannerSnapshot(BannerResponseDto.from(bannerForm.getBannerObj()))
                .createdAt(bannerForm.getCreatedAt())
                .auditedAt(bannerForm.getAuditedAt());
    }

    static public BannerFormResponseDto from(BannerForm bannerForm) {
        return base(bannerForm).build();
    }

    static public BannerFormResponseDto from(BannerForm bannerForm, BannerFormRelation relation) {
        return base(bannerForm)
                .user(relation.isUser() ? UserResponseDto.from(bannerForm.getUser()) : null)
                .build();
    }

}
