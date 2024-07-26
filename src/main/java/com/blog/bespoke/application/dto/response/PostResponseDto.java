package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostCountInfo;
import com.blog.bespoke.domain.model.post.PostRelation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<CommentResponseDto> comments;

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
                .likedByUser(Boolean.TRUE.equals(post.getLikedByUser()));
    }

    static public PostResponseDto from(Post post) {
        return base(post)
                .build();
    }

    static public PostResponseDto from(Post post, PostRelation relation) {
        return base(post)
                .category(relation.isCategory() ? post.getCategory() : null)
                .countInfo(relation.isCount() ? PostCountInfoResponseDto.from(post.getPostCountInfo()) : null)
                .author(relation.isAuthor() ? UserResponseDto.from(post.getAuthor()) : null)
                .comments(relation.isComments() ? post.getComments().stream().map(CommentResponseDto::from).collect(Collectors.toSet()) : null)
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
