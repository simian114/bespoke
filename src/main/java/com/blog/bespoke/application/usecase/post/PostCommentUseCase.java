package com.blog.bespoke.application.usecase.post;

import com.blog.bespoke.application.dto.request.CommentCreateRequestDto;
import com.blog.bespoke.application.dto.request.CommentUpdateRequestDto;
import com.blog.bespoke.application.dto.response.CommentResponseDto;
import com.blog.bespoke.domain.model.comment.Comment;
import com.blog.bespoke.domain.model.comment.CommentUpdateCmd;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.comment.CommentRepository;
import com.blog.bespoke.domain.repository.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentUseCase {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentRepository.getCommentsByPostId(postId)
                .stream().map(CommentResponseDto::from)
                .toList();
    }

    @Transactional
    public CommentResponseDto addComment(CommentCreateRequestDto requestDto, Long postId, User currentUser) {
        Post post = postRepository.getById(postId);

        Comment comment = requestDto.toModel();
        comment.setPost(post);
        comment.setUser(currentUser);
        return CommentResponseDto.from(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        postRepository.deleteById(commentId);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto requestDto, Long postId) {
        Comment comment = commentRepository.getById(commentId);
        CommentUpdateCmd cmd = CommentUpdateCmd.builder().content(requestDto.getContent()).build();
        comment.update(cmd);
        return CommentResponseDto.from(comment);
    }
}
