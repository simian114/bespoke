package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.post.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostCreateRequestDto {
    @NotEmpty
    @Size(min = 5, max = 100)
    private String title;

    private String description;
    private String content;

    public Post toModel() {
        return Post.builder()
                .title(title)
                .description(description)
                .status(Post.Status.DRAFT)
                .build();
    }
}
