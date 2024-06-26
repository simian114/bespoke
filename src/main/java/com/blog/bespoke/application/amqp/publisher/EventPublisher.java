package com.blog.bespoke.application.amqp.publisher;

import com.blog.bespoke.application.amqp.message.UserRegistrationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.routing-key.mail}")
    private String mailQueueKey;

    public void publishMailEvent(UserRegistrationMessage message) {
        rabbitTemplate.convertAndSend(mailQueueKey, message);
    }
}
