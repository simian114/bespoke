package com.blog.bespoke.infrastructure.event.config;

import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.infrastructure.event.rabbitmq.publisher.RabbitMqPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventConfig {
    private final RabbitTemplate rabbitTemplate;

    @Bean
    EventPublisher eventPublisher() {
        return new RabbitMqPublisher(rabbitTemplate);
    }
}
