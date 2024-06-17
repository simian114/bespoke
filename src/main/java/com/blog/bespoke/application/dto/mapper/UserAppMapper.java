package com.blog.bespoke.application.dto.mapper;

import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserAppMapper {
    public User toDomain(UserSignupRequestDto requestDto) {
        return User.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .name(requestDto.getName())
                .password(requestDto.getPassword())
                .build();
    }
}
