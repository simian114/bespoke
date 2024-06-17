package com.blog.bespoke.infrastructure.repository.jdbc;

import com.blog.bespoke.infrastructure.entity.jdcb.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJdbcRepository extends CrudRepository<UserEntity, Long> {
}
