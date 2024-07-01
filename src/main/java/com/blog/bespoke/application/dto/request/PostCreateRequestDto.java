package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.post.Post;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostCreateRequestDto {
    private String title;
    private String description;
    private String content;
    private Post.Status status;
    // todo: category, tags
}
