package com.blog.bespoke.domain.repository;

import com.blog.bespoke.domain.model.token.Token;
import com.blog.bespoke.domain.model.token.TokenSearchCond;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    Token save(Token token);

    Optional<Token> findByCode(String code);

    Token getByCode(String code);

    void delete(Token token);

    void deleteById(Long tokenId);

    void deleteByCode(String code);

    // TODO: pagination 으로 만료 된 토큰 가져오는 쿼리 만들기
    Page<Token> search(TokenSearchCond cond);

    void deleteAllByRefIdAndType(Long refId, Token.Type type);
}
