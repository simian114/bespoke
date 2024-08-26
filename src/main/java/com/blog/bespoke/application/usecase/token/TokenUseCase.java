package com.blog.bespoke.application.usecase.token;

import com.blog.bespoke.domain.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUseCase {
    private final TokenRepository tokenRepository;

    @Transactional
    public void deleteToken(Long tokenId) {
        tokenRepository.deleteById(tokenId);
    }
}
