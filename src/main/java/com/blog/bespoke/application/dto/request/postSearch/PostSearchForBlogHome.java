package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
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
}
