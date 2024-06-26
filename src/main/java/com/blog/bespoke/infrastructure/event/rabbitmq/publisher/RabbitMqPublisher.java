package com.blog.bespoke.infrastructure.event.rabbitmq.publisher;

import com.blog.bespoke.application.event.message.UserRegistrationMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class RabbitMqPublisher implements EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.routing-key.mail}")
    private String mailQueueKey;

    public void publishMailEvent(UserRegistrationMessage message) {
        rabbitTemplate.convertAndSend(mailQueueKey, message);
    }
}
