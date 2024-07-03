package com.blog.bespoke.application.event.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostLikeCancelMessage {
    private Long userId;
    private Long postId;
}
