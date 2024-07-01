package com.blog.bespoke.infrastructure.repository.user;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserSearchCond;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.repository.UserRepository;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.blog.bespoke.domain.model.user.QUser.user;

/**
 * Repository 는 단순히 SimpleJpaRepository 메서드의 실행만을 담당하지 않는다.
 * Repository 에서 발생한 예외를 Service layer 의 Business exception 으로 변환하는 역할을 담당한다.
 * 또한 JpaRepository 가 아닌 다른 spring Data 프레임워크 또한 같이 사용할 수 있게 만든다.
 */
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JPAQueryFactory queryFactory;
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public User save(User user) {
        // TODO: 이메일 or 닉네임 중복 시 발생하는 예외를 핸들링 해야함.
        // TODO: 그 외의 테이블 조건에 부합하지 않는 예외가 발생할 시 그 예외들도 핸들링 해야함
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public User getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException(id + " 인 user 없음"));
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public User getByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new RuntimeException(email + " 인 user 없음"));
    }

    // role
    @Override
    public Optional<Role> findRoleByCode(Role.Code code) {
        return roleJpaRepository.findByCode(code);
    }

    @Override
    public Role getRoleByCode(Role.Code code) {
        return findRoleByCode(code)
                .orElseThrow(() -> new RuntimeException(code + " 는 없음."));
    }

    // 내가 팔로우 한 사람 following
    @Override
    public Optional<User> findUserWithFollowByIdAndFollowingId(Long userId, Long followingId) {
        return userJpaRepository.findUserWithFollowByIdAndFollowingId(userId, followingId);
    }

    @Override
    public User getUserWithFollowByIdAndFollowingId(Long userId, Long followingId) {
        findUserWithFollowByIdAndFollowingId(userId, followingId);
        return findUserWithFollowByIdAndFollowingId(userId, followingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOW_NOT_FOUND));
    }

    @Override
    public Page<User> search(UserSearchCond cond, Pageable pageable) {
        JPAQuery<User> jpaQuery = query(user, cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // TODO: sort 해야함
        // orderBy 도 pageable 를 이용해서
        // pageable.getSort()
        jpaQuery.orderBy(user.createdAt.desc());
        List<User> users = jpaQuery.fetch();

        // NOTE: 1페이지, 2페이지.... 모든 페이지 가져올 때 마다 count 를 해야하나?
        Long totalSize = countQuery(cond).fetch().get(0);

        // NOTE: infinite 스크롤인 경우에는 cursor 를 내보도록 수정
        return PageableExecutionUtils.getPage(users, pageable, () -> totalSize);
    }

    private <T> JPAQuery<T> query(Expression<T> expr, UserSearchCond cond) {
        return queryFactory.select(expr)
                .from(user)
                .leftJoin(user.roles).fetchJoin()
                .where(
                        statusEq(cond != null ? cond.getStatus() : null),
                        roleEq(cond != null ? cond.getRole() : null)
                );
    }

    private JPAQuery<Long> countQuery(UserSearchCond cond) {
        return queryFactory.select(Wildcard.count)
                .from(user)
                .where(
                        statusEq(cond != null ? cond.getStatus() : null),
                        roleEq(cond != null ? cond.getRole() : null)
                );
    }

    private BooleanExpression statusEq(User.Status status) {
        if (status == null) {
            return null;
        }
        return user.status.eq(status);
    }

    private BooleanExpression roleEq(Role.Code role) {
        if (role == null) {
            return null;
        }
        return user.roles.any().role.code.eq(role);
    }

}
