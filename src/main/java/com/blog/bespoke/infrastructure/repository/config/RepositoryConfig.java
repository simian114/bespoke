package com.blog.bespoke.infrastructure.repository.config;

import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.repository.user.UserRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.user.RoleJpaRepository;
import com.blog.bespoke.infrastructure.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {
    private final UserJpaRepository userJpaRepository;

    @Bean
    public UserRepository userRepository(RoleJpaRepository roleJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository, roleJpaRepository);
    }
}
