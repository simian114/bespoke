package com.blog.bespoke.domain.model.post;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchCond extends CommonSearchCond {
    private Long authorId;
    private Post.Status status = Post.Status.PUBLISHED;
}
