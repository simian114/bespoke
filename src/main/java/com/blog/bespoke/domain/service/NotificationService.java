package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.notification.NotificationSearchCond;
import com.blog.bespoke.domain.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    /**
     * 1. 비로그인 유저는 검색할 수 없음
     * 2. recipientId 는 유저와 동일해야함.
     * 3. 어드민이면 recipientId 어떤 recipientId도 검색 가능함. null 도 가능
     */
    public boolean canSearch(NotificationSearchCond cond, User currentUser) {
        if (currentUser == null) {
            return false;
        }
        if (currentUser.isAdmin()) {
            return true;
        }
        if (!currentUser.getId().equals(cond.getRecipientId())) {
            return false;
        }
        return true;
    }
}
