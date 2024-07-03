package com.blog.bespoke.domain.repository.post;

public interface PostCountInfoRepository {
    void incrementLikeCount(Long postId);

    void decrementLikeCount(Long postId);

    void incrementViewCount(Long postId);

    void incrementCommentCount(Long postId);

    void decrementCommentCount(Long postId);
}
