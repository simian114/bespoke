package com.blog.bespoke.infrastructure.repository.comment;

import com.blog.bespoke.domain.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByPostId(Long postId);
}
