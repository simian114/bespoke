package com.blog.bespoke.domain.service;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /**
     * 1. currentUser 가 존재하면 likedByUser 가 필드에 추가됨
     * 2. currentUser 가 없으면 likedByUser 는 없음
     * 3. 조건에 따라 viewcount 를 실행함
     */
    public Post getPostAndUpdateViewCountWhenNeeded(Long postId, User currentUser) {
        Post post = postRepository.getById(postId);

        if (!canShow(post, currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        if (currentUser != null) {
            if (postRepository.existsPostLikeByPostIdAndUserId(postId, currentUser.getId())) {
                post.setLikedByUser(true);
            }
        }

        if (shouldIncreaseViewCount(post, currentUser)) {
            post.getPostCountInfo().increaseViewCount();
        }
        return post;
    }

    /**
     * 1. published post 가 아니면 조회수는 오르지 않음
     * 2. currentUser 가 어드민일 경우 조회수는 오르지 않음
     */
    private boolean shouldIncreaseViewCount(Post post, User currentUser) {
        if (!post.isPublished()) {
            return false;
        }
        if (currentUser != null && currentUser.isAdmin()) {
            return false;
        }
        return true;
    }

    private boolean canShow(Post post, User user) {
        if (post.isDeleted()) {
            return false;
        }
        if (post.getAuthor().getBannedUntil() != null) {
            return false;
        }
        if (post.isPublished()) {
            return true;
        }
        if (user == null) {
            return false;
        }
        // published 상태가 아니라면, 본인 또는 어드민만 볼 수 있음
        return post.getAuthor().getId().equals(user.getId()) || user.isAdmin();
    }

    /**
     * * -> Draft 는 안된다.
     * * -> BLOCKED 또는 BLOCKED -> * 는 admin 만 가능하다.
     */
    public boolean canUpdateStatus(Post post, Post.Status toBe, User user) {
        if (!post.getAuthor().getId().equals(user.getId()) && !user.isAdmin()) {
            return false;
        }
        if (toBe == Post.Status.DRAFT) {
            return false;
        }
        return (toBe != Post.Status.BLOCKED || user.isAdmin())
                && (!post.isBlocked() || user.isAdmin());
    }
}
