package com.blog.bespoke.domain.model.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostRelation {
    private boolean count;
    private boolean category;
    private boolean author;
    private boolean comments;
}
