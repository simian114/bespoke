package com.blog.bespoke.infrastructure.repository.token;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.token.Token;
import com.blog.bespoke.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import java.util.function.Supplier;

import java.util.Optional;

@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {
    private final TokenJpaRepository tokenJpaRepository;

    @Override
    public Token save(Token token) {
        return tokenJpaRepository.save(token);
    }

    @Override
    public Optional<Token> findByCode(String code) {
        return tokenJpaRepository.findByCode(code);
    }

    @Override
    public Token getByCode(String code) {
        return tokenJpaRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
    }

    public Token getByCode(String code, Supplier<? extends BusinessException> supplier) {
        return tokenJpaRepository.findByCode(code).orElseThrow(supplier);
    }

    @Override
    public void delete(Token token) {
        tokenJpaRepository.delete(token);
    }

    @Override
    public void deleteByCode(String code) {
        tokenJpaRepository.deleteByCode(code);
    }
}
