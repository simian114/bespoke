package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostCountInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Category category;

    private boolean likedByUser;

    static private PostResponseDtoBuilder base(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .author(UserResponseDto.from(post.getAuthor()))
                .likedByUser(Boolean.TRUE.equals(post.getLikedByUser()));
    }

    static public PostResponseDto from(Post post) {
        return base(post)
                .build();
    }

    static public PostResponseDto from(Post post, PostResponseDtoRelationJoin usage) {
        return base(post)
                .category(usage.category ? post.getCategory() : null)
                .countInfo(usage.count ? PostCountInfoResponseDto.from(post.getPostCountInfo()) : null)
                .build();
    }

    @Builder
    @Getter
    public static class PostResponseDtoRelationJoin {
        private boolean category;
        private boolean count;
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
