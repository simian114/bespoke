package com.blog.bespoke.infrastructure.repository.post;

import com.blog.bespoke.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postLikes l WHERE p.id = :postId AND p.status = 'PUBLISHED' AND l.user.id = :userId")
    Optional<Post> findPostWithLikeByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId")
    void decrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
    void incrementViewCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.commentCount = p.commentCount + 1 WHERE p.postId = :postId")
    void incrementCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCountInfo p SET p.commentCount = p.commentCount - 1 WHERE p.postId = :postId")
    void decrementCommentCount(@Param("postId") Long postId);
}
