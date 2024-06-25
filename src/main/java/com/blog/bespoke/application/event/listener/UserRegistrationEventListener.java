package com.blog.bespoke.application.event.listener;

import com.blog.bespoke.application.event.type.UserRegistrationEvent;
import com.blog.bespoke.application.usecase.MailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@EnableAsync
@Component
public class UserRegistrationEventListener {
    private final MailUseCase mailUseCase;

    @Async
    @EventListener
    public void listen(UserRegistrationEvent event) {
        mailUseCase.sendVerificationCode(event.getEmail(), event.getCode());
    }

}
