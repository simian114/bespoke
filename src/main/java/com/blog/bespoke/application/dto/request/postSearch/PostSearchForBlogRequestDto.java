package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.service.cache.PostCacheService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostSearchForBlogRequestDto implements PostSearchRequestDto {
    private Long category;
    private Integer page;

    public PostSearchCond toModel() {
        PostSearchCond postSearchCond = new PostSearchCond();
        postSearchCond.setPage(page != null ? page : 0);
        postSearchCond.setCategory(category);
        postSearchCond.setPageSize(20);
        return postSearchCond;
    }
}
