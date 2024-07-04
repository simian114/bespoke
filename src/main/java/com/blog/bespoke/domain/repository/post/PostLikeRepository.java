package com.blog.bespoke.domain.repository.post;

public interface PostLikeRepository {
    boolean existsPostLikeByPostIdAndUserId(Long postId, Long userId);
}
