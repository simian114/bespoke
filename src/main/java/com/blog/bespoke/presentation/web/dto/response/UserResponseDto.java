package com.blog.bespoke.presentation.web.dto.response;

import com.blog.bespoke.domain.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String name;

    static public UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .build();
    }
}
