package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.comment.Comment;
import lombok.Data;

@Data
public class CommentCreateRequestDto {
    private String content;

    public Comment toModel() {
        return Comment.builder()
                .content(this.content)
                .build();
    }

}
