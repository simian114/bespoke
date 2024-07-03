package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostCountInfo;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Data
public class PostResponseDto {
    private Long id;
    private String title;
    private String description;
    private String content;
    private Post.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostCountInfoResponseDto countInfo;
    private UserResponseDto author;

    static public PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .countInfo(PostCountInfoResponseDto.from(post.getPostCountInfo()))
                .author(UserResponseDto.from(post.getAuthor()))
                .build();
    }

    @Setter
    @Getter
    @Builder
    public static class PostCountInfoResponseDto {
        private long likeCount;
        private long viewCount;
        private long commentCount;

        static public PostCountInfoResponseDto from(PostCountInfo info) {
            if (info == null) {
                return PostCountInfoResponseDto.builder().build();
            }
            return PostCountInfoResponseDto.builder()
                    .viewCount(info.getViewCount())
                    .likeCount(info.getLikeCount())
                    .commentCount(info.getCommentCount())
                    .build();
        }
    }
}
