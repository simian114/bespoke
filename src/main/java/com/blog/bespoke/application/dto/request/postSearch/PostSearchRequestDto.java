package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;

public interface PostSearchRequestDto {
    String toString();

    PostSearchCond toModel();
}
