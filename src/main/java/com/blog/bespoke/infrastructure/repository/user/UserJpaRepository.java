package com.blog.bespoke.infrastructure.repository.user;

import com.blog.bespoke.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.followings f WHERE f.followerId = :userId AND f.followingId = :followingId")
    Optional<User> findUserWithFollowByIdAndFollowingId(@Param("userId") Long userId, @Param("followingId") Long followingId);

    @Query("SELECT u FROM User u LEFT JOIN Follow f on u.id = f.followingId WHERE u.id = :userId AND f.followerId = :followerId")
    Optional<User> findUserWithFollowByIdAndFollowerId(@Param("userId") Long userId, @Param("followerId") Long followerId);

    @Query("SELECT distinct u from User u left join fetch u.followers where u.id = :userId")
    Optional<User> findByidWithFollowers(@Param("userId") Long userId);


    @Modifying
    @Query("UPDATE UserCountInfo u SET u.followerCount = u.followerCount + 1 WHERE u.userId =:userId")
    void incrementFollowerCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserCountInfo u SET u.followingCount = u.followingCount + 1 WHERE u.userId = :userId")
    void incrementFollowingCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserCountInfo  u SET u.followerCount = u.followerCount - 1 WHERE u.userId = :userId")
    void decrementFollowerCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserCountInfo u SET u.followingCount = u.followingCount - 1 WHERE u.userId = :userId")
    void decrementFollowingCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserCountInfo u Set u.publishedPostCount = u.publishedPostCount + 1 WHERE u.userId = :userId")
    void incrementPublishedPostCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserCountInfo u SET u.publishedPostCount = u.publishedPostCount - 1 WHERE u.userId = :userId")
    void decrementPublishedPostCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserCountInfo u SET u.likePostCount = u.likePostCount + 1 WHERE u.userId = :userId")
    void incrementLikePostCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserCountInfo u SET u.likePostCount = u.likePostCount - 1 WHERE u.userId = :userId")
    void decrementLikePostCount(@Param("userId") Long userId);

}
