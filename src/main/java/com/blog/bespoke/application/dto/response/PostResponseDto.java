package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostCountInfo;
import com.blog.bespoke.domain.model.post.PostRelation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponseDto {
    private Long id;
    private String title;
    private String description;
    private String content;
    private Post.Status status;

    private String createdAt;

    private String updatedAt;

    private PostCountInfoResponseDto countInfo;
    private UserResponseDto author;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private S3PostImageResponseDto cover;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<S3PostImageResponseDto> images;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponseDto.CategoryResponseDto category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<CommentResponseDto> comments;

    private boolean likedByUser;

    static private PostResponseDtoBuilder base(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt().format(formatter))
                .updatedAt(post.getUpdatedAt().format(formatter))
                .likedByUser(Boolean.TRUE.equals(post.getLikedByUser()));
    }

    static public PostResponseDto from(Post post) {
        return base(post)
                .build();
    }

    static public PostResponseDto from(Post post, PostRelation relation) {
        return base(post)
                .category(relation.isCategory() && post.getCategory() != null
                        ? UserResponseDto.CategoryResponseDto.from(post.getCategory())
                        : null)
                .countInfo(relation.isCount() ? PostCountInfoResponseDto.from(post.getPostCountInfo()) : null)
                .author(relation.isAuthor() ? UserResponseDto.from(post.getAuthor()) : null)
                .comments(relation.isComments() ? post.getComments().stream().map(CommentResponseDto::from).collect(Collectors.toSet()) : null)
                .images(relation.isImages() ? post.getImages().stream().map(S3PostImageResponseDto::from).collect(Collectors.toSet()) : null)
                .cover(relation.isCover() && post.getCover() != null ? S3PostImageResponseDto.from(post.getCover()) : null)
                .build();
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
