package com.blog.bespoke.domain.service.post;

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
     * 4. manage 가 true 면 반드시 authorId 또는 nickname 을 제공해야 하고, 본인이여야한다.
     */
    public boolean canSearch(PostSearchCond cond, User currentUser) {
        if (currentUser == null) {
            return cond.getStatus() == null || cond.getStatus() == Post.Status.PUBLISHED;
        }
        if (cond.getStatus() == Post.Status.PUBLISHED) {
            return true;
        }
        if (cond.getAuthorId() != null && !cond.getAuthorId().equals(currentUser.getId())) {
            return false;
        }
        if (cond.getManage() != null && cond.getManage()) {
            if (currentUser.getNickname().equals(cond.getNickname())) {
                return true;
            }
            return false;
        }
        return true;
    }
}

