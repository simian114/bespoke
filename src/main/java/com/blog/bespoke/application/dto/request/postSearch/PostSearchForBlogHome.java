package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.service.cache.PostCacheService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PostSearchForBlogHome implements PostSearchRequestDto {
    private String nickname;

    @Override
    public PostSearchCond toModel() {
        PostSearchCond cond = new PostSearchCond();
        cond.setNickname(nickname);
        cond.setOrderBy(PostSearchCond.OrderBy.LIKE);
        cond.setPageSize(10);
        cond.setPage(0);
        return cond;
    }

    @Override
    public PostCacheService.PostSearchCacheType getType() {
        return PostCacheService.PostSearchCacheType.BLOG_HOME;
    }
}
