package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.service.cache.PostCacheService;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class PostSearchForManageRequestDto implements PostSearchRequestDto {
    private Integer page;
    private Long categoryId;
    private String nickname;

    @Override
    public PostCacheService.PostSearchCacheType getType() {
        return null;
    }

    public PostSearchCond toModel() {
        PostSearchCond postSearchCond = new PostSearchCond();
        postSearchCond.setPage(page != null ? page : 0);
        postSearchCond.setCategory(categoryId);
        postSearchCond.setPageSize(20);
        postSearchCond.setNickname(nickname);
        postSearchCond.setManage(true);
        return postSearchCond;
    }
}
