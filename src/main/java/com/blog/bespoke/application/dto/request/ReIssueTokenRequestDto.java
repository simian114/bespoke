package com.blog.bespoke.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReIssueTokenRequestDto {
    @NotEmpty
    private String refreshToken;
}
