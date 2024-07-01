package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    public boolean canShow(Post post, User user) {
        if (post.isPublished()) {
            return true;
        }
        // published 상태가 아니라면, 본인 또는 어드민만 볼 수 있음
        return post.getAuthor().getId().equals(user.getId()) || user.isAdmin();
    }
}
