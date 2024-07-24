package com.blog.bespoke.domain.repository.notification;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.notification.NotificationSearchCond;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    Notification getById(Long id) throws BusinessException;

    List<Notification> findAllByUserId(Long userId);

    void saveAll(Iterable<Notification> notifications);

    Page<Notification> search(NotificationSearchCond cond);

    void readNotification(Long notificationId);
}
