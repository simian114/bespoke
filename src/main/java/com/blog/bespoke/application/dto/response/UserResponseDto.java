package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserCountInfo;
import lombok.*;

@Builder
@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String name;
    private UserCountInfoResponseDto countInfo;

    static public UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .countInfo(UserCountInfoResponseDto.from(user.getUserCountInfo()))
                .build();
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
                return UserCountInfoResponseDto.builder().build();
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
