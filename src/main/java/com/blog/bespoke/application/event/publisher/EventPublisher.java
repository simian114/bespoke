package com.blog.bespoke.application.event.publisher;

import com.blog.bespoke.application.event.message.*;

public interface EventPublisher {
    // 이메일 관련 이벤트(공통화)
    void publishMailEvent(UserRegistrationMessage message);

    // NOTE: 팔로우 시 발생
    void publishFollowEvent(UserFollowMessage message);

    // NOTE: 언팔로우 시 발생
    void publishUnfollowEvent(UserUnFollowMessage message);

    // NOTE: create 가 아닌 published 이벤트로 변경해야함
    void publishPostPublishEvent(PostCreateMessage message);

    // NOTE:
    void publishPostLikeEvent(PostLikeMessage message);
}
