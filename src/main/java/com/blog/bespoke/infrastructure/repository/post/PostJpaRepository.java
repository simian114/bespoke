package com.blog.bespoke.infrastructure.repository.post;

import com.blog.bespoke.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.postLikes l WHERE p.id = :postId AND p.status = 'PUBLISHED' AND l.user.id = :userId")
    Optional<Post> findPostWithLikeByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
