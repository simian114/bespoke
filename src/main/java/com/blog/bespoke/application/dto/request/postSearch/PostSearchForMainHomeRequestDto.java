package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class PostSearchForMainHomeRequestDto implements PostSearchRequestDto {
    private Integer page;
    private Integer size;
    private PostSearchCond.OrderBy orderBy;

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public PostSearchCond.OrderBy getOrderBy() {
        return orderBy == null ? PostSearchCond.OrderBy.LIKE : orderBy;
    }

    public PostSearchCond toModel() {
        PostSearchCond cond = new PostSearchCond();
        cond.setPageSize(size != null ? size : 20);
        cond.setOrderBy(orderBy != null ? orderBy : PostSearchCond.OrderBy.LIKE);
        cond.setPage(page != null ? page : 0);
        return cond;
    }
}
