package com.blog.bespoke.application.dto.request.postSearch;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PostSearchForAdmin implements PostSearchRequestDto {
    private final String title;
    private final String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate from;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate to;

    private final List<Post.Status> statuses;

    private final Integer page;

    @Override
    public PostSearchCond toModel() {
        PostSearchCond postSearchCond = new PostSearchCond();
        postSearchCond.setNickname(nickname);
        postSearchCond.setTitle(title);
        postSearchCond.setFrom(from != null ? from.atStartOfDay() : null);
        postSearchCond.setTo(to != null ? to.atTime(23, 59, 59) : null);
        postSearchCond.setStatuses(statuses);
        postSearchCond.setPage(page);
        if (statuses == null) {
            postSearchCond.setStatuses(List.of());
        }
        return postSearchCond;
    }
}
