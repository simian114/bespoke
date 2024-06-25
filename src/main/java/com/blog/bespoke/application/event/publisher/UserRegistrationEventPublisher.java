package com.blog.bespoke.application.event.publisher;

import com.blog.bespoke.application.event.type.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(String email, String code) {
        publisher.publishEvent(UserRegistrationEvent.builder()
                .email(email)
                .code(code)
                .build());
    }
}
