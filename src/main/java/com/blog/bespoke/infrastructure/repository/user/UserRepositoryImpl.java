package com.blog.bespoke.infrastructure.repository.user;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.category.QCategory;
import com.blog.bespoke.domain.model.user.*;
import com.blog.bespoke.domain.model.user.role.QRole;
import com.blog.bespoke.domain.model.user.role.QUserRole;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;
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
    public Optional<User> findById(Long userId, UserRelation relation) {
        JPQLQuery<User> query = findWithRelation(relation);
        try {
            return Optional.ofNullable(query.where(user.id.eq(userId)).fetchOne());
        } catch (NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email, UserRelation relation) {
        JPQLQuery<User> query = findWithRelation(relation);
        try {
            return Optional.ofNullable(query.where(user.email.eq(email)).fetchOne());
        } catch (NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public User getById(Long userId, UserRelation relation) {
        return findById(userId, relation)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Optional<User> findByNickname(String nickname, UserRelation relation) {
        JPQLQuery<User> query = findWithRelation(relation);
        try {
            return Optional.ofNullable(query.where(user.nickname.eq(nickname)).fetchOne());
        } catch (NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public User getUserByNickname(String nickname, UserRelation relation) {
        return findByNickname(nickname, relation)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private JPQLQuery<User> findWithRelation(UserRelation relation) {
        QUser user = QUser.user;
        QUserCountInfo userCountInfo = QUserCountInfo.userCountInfo;
        QUserProfile userProfile = QUserProfile.userProfile;
        QCategory category = QCategory.category;
        QUserRole userRole = QUserRole.userRole;
        JPQLQuery<User> query = queryFactory.selectFrom(user);
        if (relation.isCount()) {
            query.leftJoin(user.userCountInfo, userCountInfo).fetchJoin();
        }
        if (relation.isProfile()) {
            query.leftJoin(user.userProfile, userProfile).fetchJoin();
        }
        if (relation.isCategories()) {
            query.leftJoin(user.categories, category).fetchJoin();
        }
        if (relation.isRoles()) {
            query.leftJoin(user.roles, userRole).fetchJoin();
        }
        return query;
    }


    @Override
    public User save(User user) {
        try {
            User save = userJpaRepository.save(user);
            return save;
        } catch (DataIntegrityViolationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                ConstraintViolationException.ConstraintKind kind = ((ConstraintViolationException) cause).getKind();
                if (kind == ConstraintViolationException.ConstraintKind.UNIQUE) {
                    String constraintName = ((ConstraintViolationException) cause).getConstraintName();
                    if (constraintName != null && constraintName.endsWith("email")) {
                        throw new BusinessException(ErrorCode.EXISTS_EMAIL);
                    } else if (constraintName != null && constraintName.endsWith("nickname")) {
                        throw new BusinessException(ErrorCode.EXISTS_NICKNAME);
                    }
                } else {
                    throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                }

            }
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname);
    }

    @Override
    public User getByNickname(String nickname) {
        return findByNickname(nickname).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
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
    public Optional<User> findUserWithFollowByIdAndFollowerId(Long userId, Long followerId) {
        return userJpaRepository.findUserWithFollowByIdAndFollowerId(userId, followerId);
    }

    @Override
    public User getUserWithFollowByIdAndFollowerId(Long userId, Long followerId) {
        return findUserWithFollowByIdAndFollowerId(userId, followerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOW_NOT_FOUND));
    }

    @Override
    public Optional<User> findUserWithFollowers(Long userId) {
        return userJpaRepository.findByidWithFollowers(userId);
    }

    @Override
    public void incrementFollowerCount(Long userId) {
        userJpaRepository.incrementFollowerCount(userId);
    }

    @Override
    public void incrementFollowingCount(Long userId) {
        userJpaRepository.incrementFollowingCount(userId);
    }

    @Override
    public void decrementFollowerCount(Long userId) {
        userJpaRepository.decrementFollowerCount(userId);
    }

    @Override
    public void decrementFollowingCount(Long userId) {
        userJpaRepository.decrementFollowingCount(userId);
    }

    @Override
    public void incrementPublishedPostCount(Long userId) {
        userJpaRepository.incrementPublishedPostCount(userId);
    }

    @Override
    public void decrementPublishedPostCount(Long userId) {
        userJpaRepository.decrementPublishedPostCount(userId);
    }

    @Override
    public void incrementLikePostCount(Long userId) {
        userJpaRepository.incrementLikePostCount(userId);
    }

    @Override
    public void decrementLikePostCount(Long userId) {
        userJpaRepository.decrementLikePostCount(userId);
    }

    @Override
    public void incrementCommentCount(Long userId) {
        userJpaRepository.incrementCommentCount(userId);
    }

    @Override
    public void decrementCommentCount(Long userId) {
        userJpaRepository.decrementCommentCount(userId);
    }

    //    @Override
//    public Optional<User> findUserWithCategories(Long userId) {
//        return userJpaRepository.findWithCategories(userId);
//    }
//
//    @Override
//    public User getUserWithCategories(Long userId) {
//        return findUserWithCategories(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//    }

    // --- search
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
        return user.status.eq(Objects.requireNonNullElse(status, User.Status.ACTIVE));
    }

    private BooleanExpression roleEq(Role.Code role) {
        return user.roles.any().role.code.eq(Objects.requireNonNullElse(role, Role.Code.USER));
    }

}
