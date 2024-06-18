package com.blog.bespoke.infrastructure.entity.jpa.mapper;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.infrastructure.entity.jpa.UserEntity;

public class UserJpaEntityMapper {
    public UserEntity toEntity(User user) {
        return UserEntity.builder()
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

    public User toDomain(UserEntity userEntity) {
        return userEntity.toDomain();
    }
}
