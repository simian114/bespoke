package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Setter
public class BannerResponseDto {
    private Long id;
    private String name;
    private String title;
    private String subTitle;
    private String content;
    // TODO: String 이 아닌 LocalDateTime 으로 넘길 수 있게 뭔가 바꾸기
    private String createdAt;
    private UserResponseDto advertiser;

    static public BannerResponseDto from(Banner banner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return BannerResponseDto.builder()
                .id(banner.getId())
                .name(banner.getName())
                .title(banner.getTitle())
                .subTitle(banner.getSubTitle())
                .content(banner.getContent())
                .createdAt(banner.getCreatedAt().format(formatter))
                .advertiser(UserResponseDto.from(banner.getAdvertiser()))
                .build();
    }

}
