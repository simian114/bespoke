package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserCountInfo;
import com.blog.bespoke.domain.model.user.UserProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String name;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String introduce;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryResponseDto> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserCountInfoResponseDto countInfo;

    static public UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .categories(user.categories != null && !user.categories.isEmpty()
                        ? user.categories.stream().map(CategoryResponseDto::from).toList()
                        : null
                )
                .introduce(user.getUserProfile() != null ? user.getUserProfile().getIntroduce() : null)
                .countInfo(UserCountInfoResponseDto.from(user.getUserCountInfo()))
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
        private String url;
        private Integer priority;
        private LocalDateTime createdAt;

        static private CategoryResponseDto from(Category category) {
            return CategoryResponseDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .url(category.getUrl())
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
                    .followerCount(info.getFollowerCount())
                    .followingCount(info.getFollowingCount())
                    .publishedPostCount(info.getPublishedPostCount())
                    .likePostCount(info.getLikePostCount())
                    .build();
        }
    }
}
