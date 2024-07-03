package com.blog.bespoke.infrastructure.event.rabbitmq.consumer;

import com.blog.bespoke.application.event.message.*;
import com.blog.bespoke.application.usecase.NoticeUseCase;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.domain.service.UserCountInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@RabbitListener(queues = "${rabbitmq.routing-key.common}")
public class CommonQueueConsumer {
    private final PostUseCase postUseCase;
    private final NoticeUseCase noticeUseCase;
    private final UserCountInfoService userCountInfoService;

    @RabbitHandler
    public void receiveUserFollowMessage(UserFollowMessage message) {
        // NOTE: usecase 를 호출하는게 좋을듯

        try {
            userCountInfoService.incrementFollowCount(message.getFollowingId(), message.getFollowerId());
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    @RabbitHandler
    public void receiveUserUnFollowMessage(UserUnFollowMessage message) {
        try {
            userCountInfoService.decrementFollowCount(message.getFollowingId(), message.getFollowerId());
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    // usecase 를 이용해서 팔로워들한테 알림 메시지를 보내줘야함.
    // 팔로우 유저가 많을 수도 있으니, batch 방법을 사용해서 알림 보내줘야하나?
    @RabbitHandler
    public void receivePublishPostMessage(PublishPostEvent message) {
        userCountInfoService.incrementPublishedPostCount(message.getAuthorId());
        // NoticeUseCase 를 만들어서 알림 보내기
        noticeUseCase.noticeToFollowers();
    }

    @RabbitHandler
    public void receivePostLikeMessage(PostLikeMessage message) {
        try {
            userCountInfoService.incrementPostLikeCount(message.getUserId());
            noticeUseCase.noticeToUser();
        } catch (Exception e) {
            log.error("receive post like message exception", e);
        }
    }

    @RabbitHandler
    public void receivePostCancelLikeMessage(PostLikeCancelMessage message) {

        try {
            userCountInfoService.decrementPostLikeCount(message.getUserId());
        } catch (Exception e) {
            log.error("receive post cancel like message exception", e);
        }
    }
}
