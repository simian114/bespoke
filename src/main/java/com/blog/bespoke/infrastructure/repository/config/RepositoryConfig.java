package com.blog.bespoke.infrastructure.repository.config;

import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.repository.jdbc.mapper.UserJdbcEntityMapper;
import com.blog.bespoke.infrastructure.repository.jdbc.user.MyUserJdbcRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.jdbc.user.UserJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableJdbcAuditing
@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {
    private final UserJdbcRepository userJdbcRepository;

    @Bean
    public UserRepository userRepository() {
        return new MyUserJdbcRepositoryImpl(userJdbcRepository, new UserJdbcEntityMapper());
    }
}
