package com.blog.bespoke.application.usecase.post;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.event.message.PostLikeCancelMessage;
import com.blog.bespoke.application.event.message.PostLikeMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostRelation;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostLikeUseCase {
    private final PostRepository postRepository;
    private final EventPublisher publisher;

    @Transactional
    public PostResponseDto likePost(Long postId, User currentUser) {
        PostRelation relation = PostRelation.builder().count(true).build();
        Post post = postRepository.getById(postId, relation);
        if (postRepository.existsPostLikeByPostIdAndUserId(postId, currentUser.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_LIKE_POST);
        }

        post.addLike(currentUser);
        post.setLikedByUser(true);
        PostResponseDto dto = PostResponseDto.from(post, relation);
        PostResponseDto.PostCountInfoResponseDto countInfo = dto.getCountInfo();
        countInfo.setLikeCount(countInfo.getLikeCount() + 1);

        // 이벤트에서 실제 db값 증가. postCountInfo 와 userCountInfo 둘 다 해야함
        publisher.publishPostLikeEvent(new PostLikeMessage(currentUser.getId(), currentUser.getNickname(), post.getAuthor().getId(), post.getAuthor().getNickname(), postId, post.getTitle(), LocalDateTime.now()));
        return dto;
    }

    @Transactional
    public PostResponseDto cancelLikePost(Long postId, User currentUser) {
        Post post = postRepository.getPostWithLikeByPostIdAndUserId(postId, currentUser.getId());
        if (post.getPostLikes().isEmpty()) {
            throw new BusinessException(ErrorCode.POST_LIKE_NOT_FOUND);
        }
        post.cancelLike(postId, currentUser);
        post.setLikedByUser(false);
        PostResponseDto dto = PostResponseDto.from(post, PostRelation.builder().count(true).build());
        PostResponseDto.PostCountInfoResponseDto countInfo = dto.getCountInfo();
        countInfo.setLikeCount(countInfo.getLikeCount() - 1);
        // 이벤트 핸들러에서는 유저의 값 감소 & post 에서는 증가
        publisher.publishPostCancelLikeEvent(new PostLikeCancelMessage(currentUser.getId(), postId));
        return dto;
    }

}
