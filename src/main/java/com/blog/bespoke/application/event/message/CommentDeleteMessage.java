package com.blog.bespoke.application.event.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentDeleteMessage {
    private Long postId;
    private Long commentId;
    private Long userId;
}
