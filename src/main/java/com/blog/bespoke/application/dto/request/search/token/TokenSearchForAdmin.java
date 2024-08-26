package com.blog.bespoke.application.dto.request.search.token;

import com.blog.bespoke.application.dto.request.search.CommonSearchRequestDto;
import com.blog.bespoke.application.dto.response.TokenResponseDto;
import com.blog.bespoke.domain.model.token.Token;
import com.blog.bespoke.domain.model.token.TokenSearchCond;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TokenSearchForAdmin implements CommonSearchRequestDto<TokenSearchCond> {
    private List<Token.Type> types;

    @Override
    public TokenSearchCond toModel() {
        TokenSearchCond cond = new TokenSearchCond();
        cond.setTypes(types);
        return cond;
    }
}
