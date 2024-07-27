package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.post.S3PostImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3PostImageResponseDto {
    private Long id;
    private S3PostImage.Type type;
    private String url;
    private String originalFilename;
    private String filename;
    private Long size;
    private String mimeType;

    static public  S3PostImageResponseDto from(S3PostImage s3PostImage) {
        return S3PostImageResponseDto.builder()
                .id(s3PostImage.getId())
                .type(s3PostImage.getType())
                .url(s3PostImage.getUrl())
                .originalFilename(s3PostImage.getOriginalFilename())
                .filename(s3PostImage.getFilename())
                .size(s3PostImage.getSize())
                .mimeType(s3PostImage.getMimeType())
                .build();
    }
}
