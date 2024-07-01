package com.blog.bespoke.domain.model.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostStatusCmd {
    @NotNull
    private Post.Status status;
}
