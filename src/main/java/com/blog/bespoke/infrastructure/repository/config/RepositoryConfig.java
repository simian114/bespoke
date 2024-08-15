package com.blog.bespoke.infrastructure.repository.config;

import com.blog.bespoke.domain.repository.TokenRepository;
import com.blog.bespoke.domain.repository.banner.BannerRepository;
import com.blog.bespoke.domain.repository.comment.CommentRepository;
import com.blog.bespoke.domain.repository.notification.NotificationRepository;
import com.blog.bespoke.domain.repository.post.PostRepository;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.infrastructure.repository.banner.BannerJpaRepository;
import com.blog.bespoke.infrastructure.repository.banner.BannerRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.comment.CommentJpaRepository;
import com.blog.bespoke.infrastructure.repository.comment.CommentRepositoryImpl;
import com.blog.bespoke.infrastructure.repository.notification.NotificationJpaRepository;
import com.blog.bespoke.infrastructure.repository.notification.NotificationRepositoryImpl;
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
    private final NotificationJpaRepository notificationJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final BannerJpaRepository bannerJpaRepository;


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
        return new PostRepositoryImpl(jpaQueryFactory(), postJpaRepository, em);
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new TokenRepositoryImpl(tokenJpaRepository);
    }

    @Bean
    public NotificationRepository notificationRepository() {
        return new NotificationRepositoryImpl(jpaQueryFactory(), notificationJpaRepository);
    }

    @Bean
    public CommentRepository commentRepository() {
        return new CommentRepositoryImpl(commentJpaRepository);
    }

    @Bean
    public BannerRepository bannerRepository() {
        return new BannerRepositoryImpl(jpaQueryFactory(), bannerJpaRepository);
    }
}
