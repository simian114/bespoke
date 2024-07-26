package com.blog.bespoke.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentUpdateRequestDto {
    @NotBlank
    private String content;
}
