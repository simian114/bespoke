package com.blog.bespoke.domain.repository.user;

public interface UserCountInfoRepository {
    void incrementFollowerCount(Long userId);

    void incrementFollowingCount(Long userId);

    void decrementFollowerCount(Long userId);

    void decrementFollowingCount(Long userId);

    void incrementPublishedPostCount(Long userId);

    void decrementPublishedPostCount(Long userId);

    void incrementLikePostCount(Long userId);

    void decrementLikePostCount(Long userId);

    void incrementCommentCount(Long userId);

    void decrementCommentCount(Long userId);
}
