package com.blog.bespoke.domain.model.post;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchCond extends CommonSearchCond {
    private Long authorId;
    private Post.Status status = Post.Status.PUBLISHED;
    /**
     * userId: post 의 postLike 에 주어진 userId 가 좋아요 postLikes 에 postLike 하나가 담김
     * userId & like is true: userId 가 좋아요 한 게시글 리스트
     * userId & follow is true: userId 가 팔로우 한 유저들의 게시글 리스트
     */
    private Boolean like; // 내가 좋아요 한 게시글
    private Boolean follow; // 내가 팔로우 한 유저의 게시글
    private long userId;
    private OrderBy orderBy = OrderBy.LATEST; // TODO: 기본은 생성일자로

    public enum OrderBy {
        LATEST,     // 최신순
        LIKE,       // 좋아요순
        VIEW,       // 조회순
        COMMENT     // 댓글순
    }
}
