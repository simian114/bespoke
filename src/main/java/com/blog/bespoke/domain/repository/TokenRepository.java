package com.blog.bespoke.domain.repository;

import com.blog.bespoke.domain.model.token.Token;

import java.util.Optional;

public interface TokenRepository {
    Token save(Token token);

    Optional<Token> findByCode(String code);

    Token getByCode(String code);

    void deleteByCode(String code);

    // TODO: pagination 으로 만료 된 토큰 가져오는 쿼리 만들기
}
