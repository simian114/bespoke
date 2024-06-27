package com.blog.bespoke.infrastructure.event.rabbitmq.consumer;

import com.blog.bespoke.application.event.message.UserRegistrationMessage;
import com.blog.bespoke.application.usecase.MailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${rabbitmq.routing-key.mail}")
public class MailQueueConsumer {
    private final MailUseCase mailUseCase;

    @RabbitHandler
    public void receiveUserRegistrationMessage(UserRegistrationMessage message) {
        mailUseCase.sendVerificationCode(message.getEmail(), message.getCode());
    }
}
