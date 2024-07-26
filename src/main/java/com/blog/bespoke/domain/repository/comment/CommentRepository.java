package com.blog.bespoke.domain.repository.comment;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.domain.model.comment.Comment;

import java.util.Optional;

public interface CommentRepository {
    // 생성 & 수정
    Comment save(Comment comment);

    // 개별 post 를 찾을 일은.. 수정할 때?
    Optional<Comment> findById(Long id);

    Comment getById(Long id) throws BusinessException;

    void deleteById(Long id);

    void delete(Comment comment);
}
