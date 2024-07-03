package com.blog.bespoke.domain.model.post;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.*;

@Getter
@Setter
public class PostSearchCond extends CommonSearchCond {
    private Long authorId;

    private Post.Status status = Post.Status.PUBLISHED;
    /**
     * boolean 이 false 인 경우에는 무시.
     * like 와 follow 가 true 인 경우에 userId 가 없으면 무시
     */
    private Boolean like = false; // 내가 좋아요 한 게시글
    private Boolean follow = false; // 내가 팔로우 한 유저의 게시글
    private long userId;
}
