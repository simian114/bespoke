package com.blog.bespoke.application.event.type;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegistrationEvent {
    private String email;
    private String code;
}
