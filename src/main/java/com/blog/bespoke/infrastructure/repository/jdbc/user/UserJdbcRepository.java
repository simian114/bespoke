package com.blog.bespoke.infrastructure.repository.jdbc.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJdbcRepository extends CrudRepository<UserJdbcEntity, Long> {
    Optional<UserJdbcEntity> findByEmail(String email);
}
