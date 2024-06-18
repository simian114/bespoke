package com.blog.bespoke.infrastructure.repository;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.entity.jpa.mapper.UserJpaEntityMapper;
import com.blog.bespoke.infrastructure.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserJpaEntityMapper userEntityMapper;

    @Override
    public User save(User user) {
        return userEntityMapper.toDomain(userJpaRepository.save(userEntityMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userEntityMapper::toDomain);
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
        userJpaRepository.delete(userEntityMapper.toEntity(user));
    }
}
