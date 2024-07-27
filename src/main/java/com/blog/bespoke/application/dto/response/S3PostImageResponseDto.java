package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.post.S3PostImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3PostImageResponseDto {
    private String location;

    static public  S3PostImageResponseDto from(S3PostImage s3PostImage) {
        return S3PostImageResponseDto.builder()
                .location(s3PostImage.getUrl())
                .build();
    }
}
