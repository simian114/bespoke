package com.blog.bespoke.domain.repository;


import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    User getById(Long id); // TODO: throw exception
    Optional<User> findByEmail(String email);
    User getByEmail(String email);
    void deleteById(Long id);
    void delete(User user);


    // role
     Optional<Role> findRoleByCode(Role.CODE code);

    Role getRoleByCode(Role.CODE code);
}
