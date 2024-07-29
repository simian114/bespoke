package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.user.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Builder
@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String name;
    private String avatarUrl;

    private UserProfileResponseDto userProfile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String introduce;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryResponseDto> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserCountInfoResponseDto countInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private S3UserAvatarResponseDto avatar;

    static private UserResponseDtoBuilder base(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName());
    }

    static public UserResponseDto from(User user) {
        return base(user).build();
    }

    static public UserResponseDto from(User user, UserRelation relation) {
        return base(user)
                .categories(relation.isCategories() && user.categories != null && !user.categories.isEmpty()
                        ? user.categories.stream()
                        .sorted(Comparator.comparing(Category::getPriority).reversed()
                                .thenComparing(Category::getCreatedAt))
                        .map(CategoryResponseDto::from)
                        .toList()
                        : null
                )
                .countInfo(relation.isCount() ? UserCountInfoResponseDto.from(user.getUserCountInfo()) : null)
                .userProfile(relation.isProfile()&& user.getUserProfile() != null ? UserProfileResponseDto.from(user.getUserProfile()) : null)
                .introduce(relation.isProfile() && user.getUserProfile() != null ? user.getUserProfile().getIntroduce() : null)
                .avatar(relation.isAvatar() && user.getAvatar() != null ? S3UserAvatarResponseDto.from(user.getAvatar()) : null)
                .avatarUrl(relation.isAvatar() && user.getAvatar() != null ? user.getAvatar().getUrl() : null)
                .build();
    }

    @AllArgsConstructor
    @Data
    @Builder
    public static class UserProfileResponseDto {
        private String introduce;

        static private UserProfileResponseDto from(UserProfile profile) {
            return UserProfileResponseDto.builder()
                    .introduce(profile.getIntroduce())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class CategoryResponseDto {
        private Long id;
        private String name;
        private String description;
        private boolean visible;
        private String url;
        private Integer priority;
        private LocalDateTime createdAt;

        static private CategoryResponseDto from(Category category) {
            return CategoryResponseDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .url(category.getUrl())
                    .visible(category.isVisible())
                    .priority(category.getPriority())
                    .createdAt(category.getCreatedAt())
                    .build();
        }
    }

    @AllArgsConstructor
    @Data
    @Builder
    public static class UserCountInfoResponseDto {
        private long followerCount;
        private long followingCount;
        private long publishedPostCount;
        private long likePostCount;
        private long commentCount;


        static public UserCountInfoResponseDto from(UserCountInfo info) {
            if (info == null) {
                return null;
            }
            return UserCountInfoResponseDto.builder()
                    .followerCount(info.getFollowerCount() == null ? 0 : info.getFollowerCount())
                    .followingCount(info.getFollowingCount() == null ? 0 : info.getFollowingCount())
                    .publishedPostCount(info.getPublishedPostCount() == null ? 0 : info.getPublishedPostCount())
                    .likePostCount(info.getLikePostCount() == null ? 0 : info.getLikePostCount())
                    .commentCount(info.getCommentCount() == null ? 0 : info.getCommentCount())
                    .build();
        }
    }
}
