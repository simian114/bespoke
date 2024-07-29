package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.user.S3UserAvatar;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class S3UserAvatarResponseDto {
    private Long id;
    private S3UserAvatar.Type type;
    private String url;
    private String originalFilename;
    private String filename;
    private Long size;
    private String mimeType;

    static public S3UserAvatarResponseDto from(S3UserAvatar avatar) {
        return S3UserAvatarResponseDto.builder()
                .id(avatar.getId())
                .type(avatar.getType())
                .url(avatar.getUrl())
                .originalFilename(avatar.getOriginalFilename())
                .filename(avatar.getFilename())
                .size(avatar.getSize())
                .mimeType(avatar.getMimeType())
                .build();
    }
}
