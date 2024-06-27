package com.blog.bespoke.infrastructure.repository.config;

import com.blog.bespoke.domain.repository.TokenRepository;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.repository.token.TokenJpaRepository;
import com.blog.bespoke.infrastructure.repository.token.TokenRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.user.RoleJpaRepository;
import com.blog.bespoke.infrastructure.repository.user.UserJpaRepository;
import com.blog.bespoke.infrastructure.repository.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {
    private final UserJpaRepository userJpaRepository;
    private final TokenJpaRepository tokenJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl(userJpaRepository, roleJpaRepository);
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new TokenRepositoryImpl(tokenJpaRepository);
    }
}
