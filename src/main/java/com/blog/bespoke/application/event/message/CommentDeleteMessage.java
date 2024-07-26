package com.blog.bespoke.application.event.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDeleteMessage {
    private Long postId;
    private Long commentId;
    private Long userId;
}
