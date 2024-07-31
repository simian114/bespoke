package com.blog.bespoke.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class UserSignupRequestDto {
    @Email
    @NotEmpty
    private String email;

    @Size(min = 4, max = 30, message = "Password must be between 4 and 30 characters")
    private String password;

    // TODO: 특수문자금지
    @Size(min = 4, max = 30, message = "Nickname must be between 4 and 30 characters")
    @NotBlank(message = "Nickname cannot be blank")
    @Pattern(
            regexp = "^(?!.*\\s)(?!.*--)[a-zA-Z0-9]+-?[a-zA-Z0-9]+$",
            message = "Nickname can only contain letters, digits, and at most one hyphen"
    )
    private String nickname;

    @NotBlank
    private String name;

    private String introduce;

    private MultipartFile avatar;
}
