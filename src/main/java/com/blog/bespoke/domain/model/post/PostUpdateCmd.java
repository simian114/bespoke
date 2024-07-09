package com.blog.bespoke.domain.model.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

/**
 * null 이면 무시하고, null 이 아니면 값을 수정한다.
 */
@Data
@Builder
public class PostUpdateCmd {
    @NotEmpty
    private String title;
    private String description;
    private String content;
}
