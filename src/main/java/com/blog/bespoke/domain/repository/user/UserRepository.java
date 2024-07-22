package com.blog.bespoke.domain.repository.user;


import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserRelation;
import com.blog.bespoke.domain.model.user.UserSearchCond;
import com.blog.bespoke.domain.model.user.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends UserCountInfoRepository {
    User save(User user);

    Optional<User> findById(Long id, UserRelation relation);
    User getById(Long id, UserRelation relation);

    Optional<User> findById(Long id);

    User getById(Long id); // TODO: throw exception

    Optional<User> findByEmail(String email);

    Optional<User> findByEmail(String email, UserRelation relation);

    User getByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByNickname(String nickname, UserRelation relation);
    User getUserByNickname(String nickname, UserRelation relation);

    User getByNickname(String nickname);

    void deleteById(Long id);

    void delete(User user);

    Page<User> search(UserSearchCond cond, Pageable pageable);

    // NOTE: 내가 팔로우 한 사람
    Optional<User> findUserWithFollowByIdAndFollowingId(Long userId, Long followingId);

    User getUserWithFollowByIdAndFollowingId(Long userId, Long followingId);

    // NOTE: 나와 팔로워
    Optional<User> findUserWithFollowByIdAndFollowerId(Long userId, Long followerId);

    // NOTE: 나 & 모든 팔로워
    Optional<User> findUserWithFollowers(Long userId);

    User getUserWithFollowByIdAndFollowerId(Long userId, Long followerId);


    // role
    Optional<Role> findRoleByCode(Role.Code code);

    Role getRoleByCode(Role.Code code);

}
