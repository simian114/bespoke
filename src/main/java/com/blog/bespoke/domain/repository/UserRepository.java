package com.blog.bespoke.domain.repository;


import com.blog.bespoke.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    User getById(Long id); // TODO: throw exception
    void deleteById(Long id);
    void delete(User user);
}
