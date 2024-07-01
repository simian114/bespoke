package com.blog.bespoke.domain.repository;


import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserSearchCond;
import com.blog.bespoke.domain.model.user.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<User> search(UserSearchCond cond, Pageable pageable);

    // NOTE: 내가 팔로우 한 사람
    Optional<User> findUserWithFollowByIdAndFollowingId(Long userId, Long followingId);

    User getUserWithFollowByIdAndFollowingId(Long userId, Long followingId);

    // role
    Optional<Role> findRoleByCode(Role.Code code);

    Role getRoleByCode(Role.Code code);
}
