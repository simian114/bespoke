package com.blog.bespoke.application.usecase.notification;

import com.blog.bespoke.application.dto.notification.*;
import com.blog.bespoke.application.dto.response.NotificationResponseDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.notification.ExtraInfo;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.notification.NotificationSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.notification.NotificationRepository;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.domain.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationUseCase {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Transactional
    public Page<NotificationResponseDto> search(NotificationSearchCond cond, User currentUser) {
        if (!notificationService.canSearch(cond, currentUser)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        return notificationRepository.search(cond).
                map(NotificationResponseDto::from);
    }

    @Transactional
    public void createNotifications(PublishNotificationDto dto) {
        User userWithFollowers = userRepository.findUserWithFollowers(dto.getPublisherId())
                .orElse((null));
        if (userWithFollowers == null) {
            return;
        }
        Set<Notification> notifications = userWithFollowers.followers.stream()
                .map(f -> dto.toModel(userWithFollowers, f.getFollowerId()))
                .collect(Collectors.toSet());

        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void createNotification(NotificationDto dto) {
        notificationRepository.save(dto.toModel());
    }


    @Transactional
    public void readNotification(Long notificationId) {
        notificationRepository.readNotification(notificationId);
    }
}
