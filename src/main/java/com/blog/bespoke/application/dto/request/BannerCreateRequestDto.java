package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.BannerUiType;
import com.blog.bespoke.domain.model.banner.S3BannerImage;
import com.blog.bespoke.domain.model.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class BannerCreateRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String title;

    @NotBlank
    private String subTitle;

    private String content;

    @NotNull
    private BannerUiType uiType;

    @NotBlank
    private String link;

    @NotBlank
    private String backgroundColor;

    // images;
    @NotNull
    private MultipartFile pcImage;

    @NotNull
    private MultipartFile mobileImage;

    public Banner toModel(User currentUser, S3BannerImage pcImage, S3BannerImage mobileImage) {
        Set<S3BannerImage> images = Set.of(pcImage, mobileImage);
        return Banner.builder()
                .advertiser(currentUser)
                .name(name)
                .title(title)
                .subTitle(subTitle)
                .content(content)
                .bannerImages(images)
                .build();
    }
}
