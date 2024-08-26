package com.blog.bespoke.infrastructure.repository.token;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.token.QToken;
import com.blog.bespoke.domain.model.token.Token;
import com.blog.bespoke.domain.model.token.TokenSearchCond;
import com.blog.bespoke.domain.repository.TokenRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.function.Supplier;

import java.util.Optional;

@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {
    private final JPAQueryFactory queryFactory;
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
    public void deleteById(Long tokenId) {
        tokenJpaRepository.deleteById(tokenId);
    }

    @Override
    public void deleteAllByRefIdAndType(Long refId, Token.Type type) {
        tokenJpaRepository.deleteAllByRefIdAndType(refId, type);
    }

    @Override
    public void deleteByCode(String code) {
        tokenJpaRepository.deleteByCode(code);
    }

    @Override
    public Page<Token> search(TokenSearchCond cond) {
        Pageable pageable = cond.getPageable();

        JPAQuery<Token> jpaQuery = query(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        // jpaQuery.orderBy(QToken.token.createdAt.desc());
        List<Token> tokens = jpaQuery.fetch();

        Long totalSize = cond.isCount() ? countQuery(cond).fetch().get(0) : 0L;
        return PageableExecutionUtils.getPage(tokens, pageable, () -> totalSize);
    }

    private JPAQuery<Token> query(TokenSearchCond cond) {
        return queryFactory.select(QToken.token)
                .from(QToken.token)
                .where(typesIn(cond));
    }

    private JPAQuery<Long> countQuery(TokenSearchCond cond) {
        return queryFactory.select(Wildcard.count)
                .from(QToken.token)
                .where(typesIn(cond));
    }

    private BooleanExpression typesIn(TokenSearchCond cond) {
        if (cond == null || cond.getTypes() == null || cond.getTypes().isEmpty()) {
            return null;
        }
        return QToken.token.type.in(cond.getTypes());
    }



}
