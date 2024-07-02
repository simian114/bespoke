package com.blog.bespoke.application.event.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUnFollowMessage {
    private Long followingId;
    private Long followerId;
}
