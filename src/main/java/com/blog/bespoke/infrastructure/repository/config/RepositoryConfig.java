package com.blog.bespoke.infrastructure.repository.config;

import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.entity.jpa.mapper.UserJpaEntityMapper;
import com.blog.bespoke.infrastructure.repository.UserRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// mapper & repository impl
@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {
    private final UserJpaRepository userJpaRepository;

    @Bean
    public UserJpaEntityMapper userEntityMapper() {
        return new UserJpaEntityMapper();
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl(userJpaRepository, userEntityMapper());
    }
}
