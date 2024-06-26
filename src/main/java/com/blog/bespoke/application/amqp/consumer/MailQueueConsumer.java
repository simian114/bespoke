package com.blog.bespoke.application.amqp.consumer;

import com.blog.bespoke.application.amqp.message.UserRegistrationMessage;
import com.blog.bespoke.application.usecase.MailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@RabbitListener(queues = "${rabbitmq.routing-key.mail}")
public class MailQueueConsumer {
    private final MailUseCase mailUseCase;

    @RabbitHandler
    public void receiveUserRegistrationMessage(UserRegistrationMessage message) {
        mailUseCase.sendVerificationCode(message.getEmail(), message.getCode());
    }
}
