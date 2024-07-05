package com.blog.bespoke.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSignupRequestDto {
    @Email
    private String email;

    @Size(min = 4, max = 30)
    private String password;

    @Size(min = 4, max = 30)
    private String nickname;

    private String name;
}
