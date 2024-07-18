package com.blog.bespoke.application.usecase.notification;

import com.blog.bespoke.application.event.message.PostLikeMessage;
import com.blog.bespoke.domain.model.notification.ExtraInfo;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.notification.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationUseCase {
    private final NotificationRepository notificationRepository;
    // TODO: SSE 리스트를 주입 받아서, 이벤트 생성 후 연결 된 emitter 가 있다면 데이터 전송하기

    // TODO: message 타입 말고, 새로운 타입 만들기
    @Transactional
    public void createNotification(PostLikeMessage message) {
        ExtraInfo extraInfo = ExtraInfo.builder()
                .recipient(message.getAuthorNickname())
                .postTitle(message.getPostTitle())
                .publisher(message.getUserNickname())
                .build();
        notificationRepository.save(Notification.builder()
                .type(Notification.NotificationType.POST_LIKE)
                .refId(message.getPostId())
                .extraInfo(extraInfo)
                .recipient(User.builder().id(message.getAuthorId()).build())
                .publisher(User.builder().id(message.getUserId()).build())
                .build());
    }

}
