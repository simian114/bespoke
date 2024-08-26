package com.blog.bespoke.application.dto.response;

import com.blog.bespoke.domain.model.token.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TokenResponseDto {
    private Long id;
    private Token.Type type;
    private Long refId;
    private Token.RefType refType;
    private String code;
    private LocalDateTime expiredAt;

    static public TokenResponseDto from(Token token) {
        return TokenResponseDto.builder()
                .id(token.getId())
                .type(token.getType())
                .refId(token.getRefId())
                .refType(token.getRefType())
                .code(token.getCode())
                .expiredAt(token.getExpiredAt())
                .build();
    }

}
