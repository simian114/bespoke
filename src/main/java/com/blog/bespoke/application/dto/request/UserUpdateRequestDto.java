package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.user.UserUpdateCmd;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdateRequestDto {
    @NotEmpty
    private String name;

    private String introduce;

    public UserUpdateCmd toCmd() {
        return UserUpdateCmd.builder()
                .name(name)
                .introduce(introduce)
                .build();
    }
}
