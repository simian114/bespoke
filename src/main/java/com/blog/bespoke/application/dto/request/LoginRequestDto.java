package com.blog.bespoke.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 6, max = 30)
    private String password;
}
