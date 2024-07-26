package com.blog.bespoke.domain.repository.comment;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.domain.model.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    // 생성 & 수정
    Comment save(Comment comment);

    // 개별 post 를 찾을 일은.. 수정할 하는 경우 필요함
    Optional<Comment> findById(Long id);

    Comment getById(Long id) throws BusinessException;

    // post 의 댓글
    List<Comment> getCommentsByPostId(Long postId);

    void deleteById(Long id);

    void delete(Comment comment);
}
