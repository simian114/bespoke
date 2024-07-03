package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.repository.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCountInfoService {
    private final PostRepository postRepository;

    @Transactional
    public void incrementLikeCount(Long postId) {
        postRepository.incrementLikeCount(postId);
    }

    @Transactional
    public void decrementLikeCount(Long postId) {
        postRepository.decrementLikeCount(postId);

    }

    @Transactional
    public void incrementViewCount(Long postId) {
        postRepository.incrementViewCount(postId);
    }

    @Transactional
    public void incrementCommentCount(Long postId) {
        postRepository.incrementCommentCount(postId);
    }

    @Transactional
    public void decrementCommentCount(Long postId) {
        postRepository.decrementCommentCount(postId);
    }
}
