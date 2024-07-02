package com.blog.bespoke.infrastructure.event.rabbitmq.consumer;

import com.blog.bespoke.application.event.message.PostCreateMessage;
import com.blog.bespoke.application.event.message.PostLikeMessage;
import com.blog.bespoke.application.event.message.UserFollowMessage;
import com.blog.bespoke.application.usecase.NoticeUseCase;
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
    private final NoticeUseCase noticeUseCase;

    @RabbitHandler
    public void receiveUserFollowMessage(UserFollowMessage message) {
        log.info("{} 가 {} 를 follow 함", message.getFollowerId(), message.getFollowingId());
    }

    // usecase 를 이용해서 팔로워들한테 알림 메시지를 보내줘야함.
    // 팔로우 유저가 많을 수도 있으니, batch 방법을 사용해서 알림 보내줘야하나?
    @RabbitHandler
    public void receiveCreatePostMessage(PostCreateMessage message) {
        // hello world
        noticeUseCase.noticeToFollowers();
        // NoticeUseCase 를 만들어서 알림 보내기
    }

    @RabbitHandler
    public void receivePostLikeMessage(PostLikeMessage message) {
        noticeUseCase.noticeToUser();
    }
}
