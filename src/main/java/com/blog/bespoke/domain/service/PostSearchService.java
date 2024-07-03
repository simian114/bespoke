package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class PostSearchService {

    /**
     * 1. 어드민이면 항상 검색 가능
     * 2. published 는 모든 경우 검색 가능함
     * 3. published 상태가 아닌 post 는 본인만 요청 가능
     */
    public boolean canSearch(PostSearchCond cond, User currentUser) {
        if (currentUser == null) {
            return cond.getStatus() == Post.Status.PUBLISHED;
        }
        if (!cond.getAuthorId().equals(currentUser.getId())) {
            return false;
        }
        return true;
    }
}
