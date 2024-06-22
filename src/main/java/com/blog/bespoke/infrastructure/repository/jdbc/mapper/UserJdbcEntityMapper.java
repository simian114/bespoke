package com.blog.bespoke.infrastructure.repository.jdbc.mapper;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.infrastructure.repository.jdbc.user.UserJdbcEntity;

public class UserJdbcEntityMapper {
    public UserJdbcEntity toEntity(User user) {
        return UserJdbcEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    public User toDomain(UserJdbcEntity userEntity) {
        return userEntity.toDomain();
    }
}
