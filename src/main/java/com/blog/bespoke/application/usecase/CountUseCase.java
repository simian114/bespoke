package com.blog.bespoke.application.usecase;

import com.blog.bespoke.domain.repository.post.PostRepository;
import com.blog.bespoke.domain.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountUseCase {
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @Transactional
    public void changeCountWhenPostLike(Long userId, Long postId) {
        userRepository.incrementLikePostCount(userId);
        postRepository.incrementLikeCount(postId);
    }

    @Transactional
    public void changeCountWhenPostLikeCancel(Long userId, Long postId) {
        userRepository.decrementLikePostCount(userId);
        postRepository.decrementLikeCount(postId);
    }

    @Transactional
    public void changeCountWhenPostPublished(Long authorId) {
        userRepository.incrementPublishedPostCount(authorId);
    }

    @Transactional
    public void changeCountWhenFollowCreated(Long followingId, Long followerId) {
        userRepository.incrementFollowerCount(followingId);
        userRepository.incrementFollowingCount(followerId);
    }

    @Transactional
    public void changeCountWhenFollowDeleted(Long followingId, Long followerId) {
        userRepository.decrementFollowerCount(followingId);
        userRepository.decrementFollowingCount(followerId);
    }

    @Transactional
    public void changeCountWhenCommentAdd(Long userId, Long postId) {
        userRepository.incrementCommentCount(userId);
        postRepository.incrementCommentCount(postId);
    }

    @Transactional
    public void changeCountWhenCommentDelete(Long userId, Long postId) {
        userRepository.decrementCommentCount(userId);
        postRepository.decrementCommentCount(postId);

    }
}
