package com.blog.bespoke.infrastructure.repository.config;

import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.entity.jpa.mapper.UserEntityMapper;
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
    public UserEntityMapper userEntityMapper() {
        return new UserEntityMapper();
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl(userJpaRepository, userEntityMapper());
    }
}
