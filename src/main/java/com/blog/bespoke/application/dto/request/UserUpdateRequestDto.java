package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.user.S3UserAvatar;
import com.blog.bespoke.domain.model.user.UserUpdateCmd;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class UserUpdateRequestDto {
    @NotEmpty
    private String name;

    private String introduce;

    private MultipartFile avatar;

    private Long prevAvatarId;
    private String prevAvatarUrl;

    public UserUpdateCmd toCmd(S3UserAvatar s3UserAvatar) {
        return UserUpdateCmd.builder()
                .name(name)
                .introduce(introduce)
                .avatar(s3UserAvatar)
                .build();
    }
}
