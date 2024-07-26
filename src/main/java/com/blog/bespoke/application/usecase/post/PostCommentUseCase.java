package com.blog.bespoke.application.usecase.post;

import com.blog.bespoke.application.dto.request.CommentCreateRequestDto;
import com.blog.bespoke.application.dto.request.CommentUpdateRequestDto;
import com.blog.bespoke.application.dto.response.CommentResponseDto;
import com.blog.bespoke.application.event.message.CommentAddMessage;
import com.blog.bespoke.application.event.message.CommentDeleteMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.comment.Comment;
import com.blog.bespoke.domain.model.comment.CommentUpdateCmd;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostRelation;
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
    private final EventPublisher eventPublisher;

    @Transactional
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentRepository.getCommentsByPostId(postId)
                .stream().map(CommentResponseDto::from)
                .toList();
    }

    @Transactional
    public CommentResponseDto addComment(CommentCreateRequestDto requestDto, Long postId, User currentUser) {
        PostRelation postRelation = PostRelation.builder().author(true).build();
        Post post = postRepository.getById(postId, postRelation);

        Comment comment = requestDto.toModel();
        comment.setPost(post);
        comment.setUser(currentUser);
        Comment savedComment = commentRepository.save(comment);

        eventPublisher.publishCommentAddEvent(
                CommentAddMessage.builder()
                        .userId(currentUser.getId())
                        .userNickname(currentUser.getNickname())
                        .commentId(savedComment.getId())
                        .postId(postId)
                        .postTitle(post.getTitle())
                        .postAuthorId(post.getAuthor().getId())
                        .postAuthorNickname(post.getAuthor().getNickname())
                        .commentContent(savedComment.getContent())
                        .build()
        );

        return CommentResponseDto.from(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long postId, User currentUser) {
        Comment comment = commentRepository.getById(commentId);
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        commentRepository.delete(comment);
        eventPublisher.publishCommentDeleteEvent(
                CommentDeleteMessage.builder()
                        .postId(postId)
                        .userId(currentUser.getId())
                        .commentId(commentId)
                        .build()
        );
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto requestDto, User currentUser) {
        Comment comment = commentRepository.getById(commentId);
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        CommentUpdateCmd cmd = CommentUpdateCmd.builder().content(requestDto.getContent()).build();
        comment.update(cmd);
        return CommentResponseDto.from(comment);
    }
}
