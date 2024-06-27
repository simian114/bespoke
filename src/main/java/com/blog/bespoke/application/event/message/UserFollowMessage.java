package com.blog.bespoke.application.event.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFollowMessage {
    private Long followingId;
    private Long followerId;
}
