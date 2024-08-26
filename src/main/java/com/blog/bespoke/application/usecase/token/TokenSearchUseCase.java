package com.blog.bespoke.application.usecase.token;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.application.dto.response.TokenResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.domain.model.token.Token;
import com.blog.bespoke.domain.model.token.TokenSearchCond;
import com.blog.bespoke.domain.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenSearchUseCase {
    private final TokenRepository tokenRepository;

    @Transactional
    public CommonSearchResponseDto<TokenResponseDto> search(CommonSearchRequestDto<TokenSearchCond> cond) {
        Page<Token> search = tokenRepository.search(cond.toModel());
        Page<TokenResponseDto> map = search.map(TokenResponseDto::from);
        return (new CommonSearchResponseDto<TokenResponseDto>()).from(map);
    }

}
