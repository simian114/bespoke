package com.blog.bespoke.infrastructure.repository.user;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Repository 는 단순히 SimpleJpaRepository 메서드의 실행만을 담당하지 않는다.
 * Repository 에서 발생한 예외를 Service layer 의 Business exception 으로 변환하는 역할을 담당한다.
 * 또한 JpaRepository 가 아닌 다른 spring Data 프레임워크 또한 같이 사용할 수 있게 만든다.
 */
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
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
    public Optional<Role> findRoleByCode(Role.CODE code) {
        return roleJpaRepository.findByCode(code);
    }

    @Override
    public Role getRoleByCode(Role.CODE code) {
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
}
