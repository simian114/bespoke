package com.blog.bespoke.application.usecase.post;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.event.message.PostLikeMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeUseCase {
    private final PostRepository postRepository;
    private final EventPublisher publisher;

    @Transactional
    public PostResponseDto likePost(Long postId, User currentUser) {
        Optional<Post> optionalPost = postRepository.findPostWithLikeByPostIdAndUserId(postId, currentUser.getId());
        if (optionalPost.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_LIKE_POST);
        }
        Post post = postRepository.getById(postId);

        post.addLike(currentUser);
        // TODO: 좋아요 수 증가

        publisher.publishPostLikeEvent(new PostLikeMessage(currentUser.getId(), postId, LocalDateTime.now()));
        return PostResponseDto.from(post);
    }

    @Transactional
    public PostResponseDto cancelLikePost(Long postId, User currentUser) {
        Post post = postRepository.getPostWithLikeByPostIdAndUserId(postId, currentUser.getId());
        post.cancelLike(postId, currentUser);
        // 좋아요 수 감소
        return PostResponseDto.from(post);
    }

}
