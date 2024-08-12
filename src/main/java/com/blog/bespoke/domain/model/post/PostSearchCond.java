package com.blog.bespoke.domain.model.post;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostSearchCond extends CommonSearchCond {
    private Long authorId;
    private String nickname; // author nickname
    private Post.Status status;

    private String title;
    private LocalDateTime from;
    private LocalDateTime to;

    private List<Post.Status> statuses; // status list

    /**
     * category 가 있음 이걸로만 검색하면 된다.
     * category 는 유저마다 있기 때문
     */
    private Long category;

    /**
     * userId: post 의 postLike 에 주어진 userId 가 좋아요 postLikes 에 postLike 하나가 담김
     * userId & like is true: userId 가 좋아요 한 게시글 리스트
     * userId & follow is true: userId 가 팔로우 한 유저들의 게시글 리스트
     */
    private Boolean like; // 내가 좋아요 한 게시글
    private Boolean follow; // 내가 팔로우 한 유저의 게시글
    private long userId;
    private OrderBy orderBy = OrderBy.LATEST; // TODO: 기본은 생성일자로

    /**
     * 내 게시글 관리 페이지에서 호출
     * true 이면서 본인이면 모든 상태의 게시글 호출
     * status 를 주지 않으면 모든 상태의 게시글 호출
     * manage 가 true 면 반드시 authorId 또는 nickname 을 제공해야한다.
     * manage 가 true 면 서비스 레이어에서 currentUser 와 nickname 또는 authorId 를 비교해서 검증한다.
     */
    private Boolean manage;

    public enum OrderBy {
        LATEST,     // 최신순
        LIKE,       // 좋아요순
        VIEW,       // 조회순
        COMMENT     // 댓글순
    }

}
