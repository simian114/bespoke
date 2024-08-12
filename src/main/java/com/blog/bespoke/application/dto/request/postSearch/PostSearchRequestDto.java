package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.service.cache.PostCacheService;

public interface PostSearchRequestDto {
    String toString();

    PostSearchCond toModel();
}
