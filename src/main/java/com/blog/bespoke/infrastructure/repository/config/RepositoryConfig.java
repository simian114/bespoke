package com.blog.bespoke.infrastructure.repository.config;

import com.blog.bespoke.domain.repository.PostRepository;
import com.blog.bespoke.domain.repository.TokenRepository;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.repository.post.PostJpaRepository;
import com.blog.bespoke.infrastructure.repository.post.PostRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.token.TokenJpaRepository;
import com.blog.bespoke.infrastructure.repository.token.TokenRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.user.RoleJpaRepository;
import com.blog.bespoke.infrastructure.repository.user.UserJpaRepository;
import com.blog.bespoke.infrastructure.repository.user.UserRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    private final PostJpaRepository postJpaRepository;

    @PersistenceContext
    private final EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl(jpaQueryFactory(), userJpaRepository, roleJpaRepository);
    }

    @Bean
    public PostRepository postRepository() {
        return new PostRepositoryImpl(jpaQueryFactory(), postJpaRepository);
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new TokenRepositoryImpl(tokenJpaRepository);
    }
}
