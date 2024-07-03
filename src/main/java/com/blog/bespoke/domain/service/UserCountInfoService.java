package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.user.UserCountInfo;
import com.blog.bespoke.domain.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCountInfoService {
    private static final String COUNT_INFO_FOLLOWER_COUNT = "userCountInfo:followerCount:";
    private static final String COUNT_INFO_FOLLOWING_COUNT = "userCountInfo:followingCount:";
    private static final String COUNT_INFO_PUBLISHED_POST_COUNT = "userCountInfo:publishedPostCount:";
    private static final String COUNT_INFO_LIKE_POST_COUNT = "userCountInfo:likePostCount:";

    // TODO: 변경해야함
    private static final String BATCH = "need_post_update";

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    @Transactional
    public void incrementFollowCount(Long followingId, Long followerId) {
        // 찾아서 하는데, 하나의 쿼리로 하는게 좋을듯?
        userRepository.incrementFollowerCount(followingId);
        userRepository.incrementFollowingCount(followerId);
    }

    @Transactional
    public void decrementFollowCount(Long followingId, Long followerId) {
        userRepository.decrementFollowerCount(followingId);
        userRepository.decrementFollowingCount(followerId);
    }

    @Transactional
    public void incrementPublishedPostCount(Long userId) {
        userRepository.incrementPublishedPostCount(userId);
    }

    @Transactional
    public void decrementPublishedPostCount(Long userId) {
        userRepository.decrementPublishedPostCount(userId);
    }

    public void incrementPostLikeCount() {
    }

    public void decrementPostLikeCount() {

    }

}
