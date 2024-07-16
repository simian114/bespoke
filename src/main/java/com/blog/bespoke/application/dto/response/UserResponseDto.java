package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserCountInfo;
import com.blog.bespoke.domain.model.user.UserProfile;
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
    private UserProfileResponseDto userProfile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String introduce;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryResponseDto> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserCountInfoResponseDto countInfo;

    static private UserResponseDtoBuilder base(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .userProfile(user.getUserProfile() != null ? UserProfileResponseDto.from(user.getUserProfile()) : null)
                .introduce(user.getUserProfile() != null ? user.getUserProfile().getIntroduce() : null)
                .countInfo(UserCountInfoResponseDto.from(user.getUserCountInfo()));
    }

    static public UserResponseDto from(User user) {
        return base(user).build();
    }

    static public UserResponseDto from(User user, boolean withCategories) {
        return base(user)
                .categories(withCategories && user.categories != null && !user.categories.isEmpty()
                        ? user.categories.stream()
                        .sorted(Comparator.comparing(Category::getPriority).reversed()
                                .thenComparing(Category::getCreatedAt))
                        .map(CategoryResponseDto::from)
                        .toList()
                        : null
                )
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

        static public UserCountInfoResponseDto from(UserCountInfo info) {
            if (info == null) {
                return null;
            }
            return UserCountInfoResponseDto.builder()
                    .followerCount(info.getFollowerCount() == null ? 0 : info.getFollowerCount())
                    .followingCount(info.getFollowingCount() == null ? 0 : info.getFollowingCount())
                    .publishedPostCount(info.getPublishedPostCount() == null ? 0 : info.getPublishedPostCount())
                    .likePostCount(info.getLikePostCount() == null ? 0 : info.getLikePostCount())
                    .build();
        }
    }
}
