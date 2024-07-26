package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.comment.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentCreateRequestDto {
    @NotBlank
    private String content;

    public Comment toModel() {
        return Comment.builder()
                .content(this.content)
                .build();
    }

}
