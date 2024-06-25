package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.token.Token;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final TokenRepository tokenRepository;

    public Token createRefreshToken(User user) {
        return tokenRepository.save(Token.builder()
                .refId(user.getId())
                .type(Token.Type.REFRESH_TOKEN)
                .refType(Token.RefType.USER)
                .code(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusMonths(1))
                .build());
    }
}
