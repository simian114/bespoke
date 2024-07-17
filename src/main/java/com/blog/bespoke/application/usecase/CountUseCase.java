package com.blog.bespoke.application.usecase;

import com.blog.bespoke.domain.repository.post.PostRepository;
import com.blog.bespoke.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountUseCase {
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public void changeCountWhenPostLike(Long userId, Long postId) {
        userRepository.incrementLikePostCount(userId);
        postRepository.incrementLikeCount(postId);
    }

    public void changeCountWhenPostLikeCancel(Long userId, Long postId) {
        userRepository.decrementLikePostCount(userId);
        postRepository.decrementLikeCount(postId);
    }

    public void changeCountWhenPostPublished(Long authorId) {
        userRepository.incrementPublishedPostCount(authorId);
    }

    public void changeCountWhenFollowCreated(Long followingId, Long followerId) {
        userRepository.incrementFollowerCount(followingId);
        userRepository.incrementFollowingCount(followerId);
    }

    public void changeCountWhenFollowDeleted(Long followingId, Long followerId) {
        userRepository.decrementFollowerCount(followingId);
        userRepository.decrementFollowingCount(followerId);
    }
}
