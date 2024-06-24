package com.blog.bespoke.infrastructure.repository.user;

import com.blog.bespoke.domain.model.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(Role.CODE code);
}
