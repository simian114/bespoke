package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.comment.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private UserResponseDto user;
    private LocalDateTime createdAt;

    static public CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .user(UserResponseDto.from(comment.getUser()))
                .build();
    }
}
