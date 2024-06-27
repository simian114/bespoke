package com.blog.bespoke.infrastructure.repository.user;

import com.blog.bespoke.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.followings f WHERE f.followerId = :userId AND f.followingId = :followingId")
    Optional<User> findUserWithFollowByIdAndFollowingId(@Param("userId") Long userId, @Param("followingId") Long followingId);
}
