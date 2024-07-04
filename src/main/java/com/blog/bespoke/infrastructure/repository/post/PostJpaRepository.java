package com.blog.bespoke.infrastructure.repository.post;

import com.blog.bespoke.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postLikes pl WHERE p.id = :postId AND (pl IS NULL OR pl.user.id = :userId) AND p.status = 'PUBLISHED'")
    Optional<Post> findPostWithLikeByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("select count(pl) > 0 from PostLike  pl where pl.post.id = :postId and pl.user.id = :userId")
    boolean existsPostLikeByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId")
    void decrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.commentCount = p.commentCount + 1 WHERE p.postId = :postId")
    void incrementCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.commentCount = p.commentCount - 1 WHERE p.postId = :postId")
    void decrementCommentCount(@Param("postId") Long postId);
}
