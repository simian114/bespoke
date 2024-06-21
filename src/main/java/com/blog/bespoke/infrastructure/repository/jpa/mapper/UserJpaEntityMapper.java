package com.blog.bespoke.infrastructure.repository.jpa.mapper;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.infrastructure.repository.jpa.user.UserJpaEntity;

public class UserJpaEntityMapper {
    public UserJpaEntity toEntity(User user) {
        return UserJpaEntity.builder()
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

    public User toDomain(UserJpaEntity userEntity) {
        return userEntity.toDomain();
    }
}
