package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.post.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostUpdateRequestDto {
    @Size(min = 5, max = 100)
    @NotEmpty
    private String title;

    private String description;
    private String content;
    private Long categoryId;

    private Post.Status status;
}
