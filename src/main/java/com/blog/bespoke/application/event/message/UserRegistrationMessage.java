package com.blog.bespoke.application.event.message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserRegistrationMessage implements Serializable {
    private String email;
    private String code;
}
