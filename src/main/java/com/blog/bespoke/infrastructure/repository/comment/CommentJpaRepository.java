package com.blog.bespoke.infrastructure.repository.comment;

import com.blog.bespoke.domain.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

}
