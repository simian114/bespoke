package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.comment.Comment;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private UserResponseDto userResponseDto;

    static public CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userResponseDto(UserResponseDto.from(comment.getUser()))
                .build();
    }
}
