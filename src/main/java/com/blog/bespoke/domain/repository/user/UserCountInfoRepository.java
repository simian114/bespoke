package com.blog.bespoke.domain.repository.user;

public interface UserCountInfoRepository {
    void incrementFollowerCount(Long userId);

    void incrementFollowingCount(Long userId);

    void decrementFollowerCount(Long userId);

    void decrementFollowingCount(Long userId);
}
