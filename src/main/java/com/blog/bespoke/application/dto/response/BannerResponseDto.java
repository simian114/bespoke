package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.BannerUiType;
import com.blog.bespoke.domain.model.banner.S3BannerImage;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponseDto {
    private Long id;
    private String name;
    private String title;
    private String subTitle;
    private String content;
    private String link;
    private String backgroundColor;
    // TODO: String 이 아닌 LocalDateTime 으로 넘길 수 있게 뭔가 바꾸기
    private String createdAt;
    private UserResponseDto advertiser;
    private BannerUiType uiType;
    private String pcImage;
    private String mobileImage;

    static public BannerResponseDto from(Banner banner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        Set<S3BannerImage> bannerImages = banner.getBannerImages();
        S3BannerImage pcImage = bannerImages == null ? null : bannerImages.stream().filter(S3BannerImage::isPCImage).findFirst().orElse(null);
        S3BannerImage mobileImage = bannerImages == null ? null : bannerImages.stream().filter(S3BannerImage::isMobileImage).findFirst().orElse(null);

        return BannerResponseDto.builder()
                .id(banner.getId())
                .name(banner.getName())
                .title(banner.getTitle())
                .subTitle(banner.getSubTitle())
                .content(banner.getContent())
                .link(banner.getLink())
                .uiType(banner.getUiType())
                .backgroundColor(banner.getBackgroundColor())
                .pcImage(pcImage == null ? "" : pcImage.getUrl())
                .mobileImage(mobileImage == null ? "" : mobileImage.getUrl())
                .createdAt(banner.getCreatedAt() == null ? "" : banner.getCreatedAt().format(formatter))
                .advertiser(banner.getAdvertiser() == null ? null : UserResponseDto.from(banner.getAdvertiser()))
                .build();
    }

}
