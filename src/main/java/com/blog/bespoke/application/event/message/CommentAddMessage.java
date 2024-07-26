package com.blog.bespoke.application.event.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentAddMessage {
    private Long postId;
    private Long postAuthorId;
    private String postTitle;
    private Long userId;
    private Long commentId;
    private String commentContent;
}
