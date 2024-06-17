package com.blog.bespoke.infrastructure.repository.jpa;

import com.blog.bespoke.infrastructure.entity.jpa.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
