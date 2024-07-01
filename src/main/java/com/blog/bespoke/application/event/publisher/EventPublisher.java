package com.blog.bespoke.application.event.publisher;

import com.blog.bespoke.application.event.message.PostCreateMessage;
import com.blog.bespoke.application.event.message.UserFollowMessage;
import com.blog.bespoke.application.event.message.UserRegistrationMessage;

public interface EventPublisher {
    void publishMailEvent(UserRegistrationMessage message);

    void publishFollowEvent(UserFollowMessage message);

    // NOTE: create 가 아닌 published 이벤트로 변경해야함
    void publishPostPublishEvent(PostCreateMessage message);
}
