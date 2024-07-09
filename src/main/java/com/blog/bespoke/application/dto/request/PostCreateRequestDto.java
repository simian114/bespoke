package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.post.Post;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostCreateRequestDto {
    @NotEmpty
    private String title;

    private String description;
    private String content;

    private Post.Status status;
    // todo: category, tags
}
