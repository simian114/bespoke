package com.blog.bespoke.infrastructure.event.rabbitmq.publisher;

import com.blog.bespoke.application.event.message.*;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class RabbitMqPublisher implements EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.routing-key.mail}")
    private String mailQueueKey;

    @Value("${rabbitmq.routing-key.common}")
    private String commonQueueKey;

    public void publishMailEvent(UserRegistrationMessage message) {
        rabbitTemplate.convertAndSend(mailQueueKey, message);
    }

    @Override
    public void publishFollowEvent(UserFollowMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    @Override
    public void publishUnfollowEvent(UserUnFollowMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    @Override
    public void publishPostPublishEvent(PublishPostEvent message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    @Override
    public void publishPostLikeEvent(PostLikeMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    @Override
    public void publishPostCancelLikeEvent(PostLikeCancelMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    @Override
    public void publishCommentAddEvent(CommentAddMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    @Override
    public void publishCommentDeleteEvent(CommentDeleteMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    /*
     * 1. payment 객체를 생성한다.
     */
    @Override
    public void publishBannerAuditApproveEvent(BannerAuditApproveMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }

    @Override
    public void publishBannerAuditDenyEvent(BannerAuditDenyMessage message) {
        rabbitTemplate.convertAndSend(commonQueueKey, message);
    }
}
