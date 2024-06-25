package com.blog.bespoke.infrastructure.repository.token;

import com.blog.bespoke.domain.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByCode(String code);

    void deleteByCode(String code);
}
