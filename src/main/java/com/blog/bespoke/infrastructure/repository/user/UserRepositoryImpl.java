package com.blog.bespoke.infrastructure.repository.user;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.repository.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private static final String USER_CACHE_PREFIX = "user:";
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisUtil redisUtil;

    @Override
    public User save(User user) {
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
}
