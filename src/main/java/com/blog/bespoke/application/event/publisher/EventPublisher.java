package com.blog.bespoke.application.event.publisher;

import com.blog.bespoke.application.event.message.UserFollowMessage;
import com.blog.bespoke.application.event.message.UserRegistrationMessage;

public interface EventPublisher {
    void publishMailEvent(UserRegistrationMessage message);

    void publishFollowEvent(UserFollowMessage message);
}
