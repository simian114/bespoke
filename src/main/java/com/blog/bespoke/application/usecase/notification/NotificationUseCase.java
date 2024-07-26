package com.blog.bespoke.application.usecase.notification;

import com.blog.bespoke.application.dto.notification.CommentAddNotificationDto;
import com.blog.bespoke.application.dto.notification.FollowNotificationDto;
import com.blog.bespoke.application.dto.notification.PostLikeNotificationDto;
import com.blog.bespoke.application.dto.notification.PublishNotificationDto;
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
    // TODO: SSE 리스트를 주입 받아서, 이벤트 생성 후 연결 된 emitter 가 있다면 데이터 전송하기

    @Transactional
    public Page<NotificationResponseDto> search(NotificationSearchCond cond, User currentUser) {
        if (!notificationService.canSearch(cond, currentUser)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        return notificationRepository.search(cond).
                map(NotificationResponseDto::from);
    }

    // TODO: message 타입 말고, 새로운 타입 만들기
    @Transactional
    public void createNotification(PostLikeNotificationDto dto) {
        ExtraInfo extraInfo = ExtraInfo.builder()
                .recipient(dto.getAuthorNickname())
                .postTitle(dto.getPostTitle())
                .publisher(dto.getUserNickname())
                .build();
        notificationRepository.save(Notification.builder()
                .type(Notification.NotificationType.POST_LIKE)
                .refId(dto.getPostId())
                .extraInfo(extraInfo)
                .recipient(User.builder().id(dto.getAuthorId()).build())
                .publisher(User.builder().id(dto.getUserId()).build())
                .build());
    }

    @Transactional
    public void createNotification(CommentAddNotificationDto dto) {
        ExtraInfo extraInfo = ExtraInfo.builder()
                .recipient(dto.getRecipientNickname())
                .postTitle(dto.getPostTitle())
                .publisher(dto.getPublisherNickname())
                .build();
        notificationRepository.save(Notification.builder()
                .type(Notification.NotificationType.COMMENT_CREATED)
                .refId(dto.getPostId())
                .extraInfo(extraInfo)
                .recipient(User.builder().id(dto.getRecipientId()).build())
                .publisher((User.builder().id(dto.getPublisherId()).build()))
                .build());

    }

    @Transactional
    public void createNotification(FollowNotificationDto dto) {
        ExtraInfo extraInfo = ExtraInfo.builder()
                .recipient(dto.getRecipientNickname())
                .publisher(dto.getPublisherNickname())
                .build();
        notificationRepository.save(Notification.builder()
                .type(Notification.NotificationType.FOLLOW)
                .refId(null)
                .extraInfo(extraInfo)
                .recipient(User.builder().id(dto.getRecipientId()).build())
                .publisher(User.builder().id(dto.getPublisherId()).build())
                .build());
    }

    @Transactional
    public void createNotifications(PublishNotificationDto dto) {
        User userWithFollowers = userRepository.findUserWithFollowers(dto.getPublisherId())
                .orElse((null));
        if (userWithFollowers == null) {
            return;
        }
        Set<Notification> notifications = userWithFollowers.followers.stream()
                .map(f -> {
                    ExtraInfo extra = ExtraInfo.builder()
                            .publisher(userWithFollowers.getNickname())
                            .postTitle(dto.getPostTittle())
                            .build();
                    return Notification.builder()
                            .refId(dto.getPostId())
                            .publisher(User.builder().id(dto.getPublisherId()).build())
                            .recipient(User.builder().id(f.getFollowerId()).build())
                            .extraInfo(extra)
                            .type(Notification.NotificationType.POST_PUBLISHED)
                            .build();
                })
                .collect(Collectors.toSet());

        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void readNotification(Long notificationId) {
        notificationRepository.readNotification(notificationId);
    }
}
