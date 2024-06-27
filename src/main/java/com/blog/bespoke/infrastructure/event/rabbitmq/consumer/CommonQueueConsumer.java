package com.blog.bespoke.infrastructure.event.rabbitmq.consumer;

import com.blog.bespoke.application.event.message.UserFollowMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "${rabbitmq.routing-key.common}")
public class CommonQueueConsumer {
    @RabbitHandler
    public void receiveUserFollowMessage(UserFollowMessage message) {
        log.info("{} 가 {} 를 follow 함", message.getFollowerId(), message.getFollowingId());
    }
}
