package com.blog.bespoke.domain.service;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post getPostById(Long id) {
        return postRepository.getById(id);
    }

    public Post getPublishedPostById(Long id) {
        Post post = postRepository.getById(id);
        if (!post.isPublished()) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        return post;
    }

    public boolean canShow(Post post, User user) {
        if (post.isDeleted()) {
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
                && (!post.isBocked() || user.isAdmin());
    }
}
