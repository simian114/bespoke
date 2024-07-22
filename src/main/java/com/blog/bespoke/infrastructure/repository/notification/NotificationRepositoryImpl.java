package com.blog.bespoke.infrastructure.repository.notification;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public Notification getById(Long id) throws BusinessException {
        return notificationJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    @Override
    public List<Notification> findAllByUserId(Long recipientId) {
        return notificationJpaRepository.findAllByRecipientId(recipientId);
    }

    @Override
    public void saveAll(Iterable<Notification> notifications) {
        notificationJpaRepository.saveAll(notifications);
    }
}
