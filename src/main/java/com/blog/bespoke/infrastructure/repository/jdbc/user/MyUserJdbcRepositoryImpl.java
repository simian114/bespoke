package com.blog.bespoke.infrastructure.repository.jdbc.user;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.repository.jdbc.mapper.UserJdbcEntityMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MyUserJdbcRepositoryImpl implements UserRepository {
    private final UserJdbcRepository userJdbcRepository;
    private final UserJdbcEntityMapper userEntityMapper;

    @Override
    public User save(User user) {
        return userEntityMapper.toDomain(
                userJdbcRepository.save(
                        userEntityMapper.toEntity(user)
                )
        );
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserJdbcEntity> byId = userJdbcRepository.findById(id);
        return userJdbcRepository.findById(id)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public User getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException(id + " 인 user 없음"));
    }

    @Override
    public void deleteById(Long id) {
        userJdbcRepository.deleteById(id);
    }

    @Override
    public void delete(User user) {
        userJdbcRepository.delete(userEntityMapper.toEntity(user));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJdbcRepository.findByEmail(email).map(userEntityMapper::toDomain);
    }

    @Override
    public User getByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new RuntimeException(email + " 인 user 없음"));
    }
}
