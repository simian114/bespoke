package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import lombok.ToString;

@ToString
public class PostSearchForMainHomeRequestDto implements PostSearchRequestDto {
    private Integer page;
    private Integer size;
    private PostSearchCond.OrderBy orderBy;

    public PostSearchCond toModel() {
        PostSearchCond cond = new PostSearchCond();
        cond.setPageSize(size != null ? size : 20);
        cond.setOrderBy(orderBy != null ? orderBy : PostSearchCond.OrderBy.LIKE);
        cond.setPage(page != null ? page : 0);
        return cond;
    }
}
