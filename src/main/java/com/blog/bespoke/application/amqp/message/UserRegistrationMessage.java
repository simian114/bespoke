package com.blog.bespoke.application.amqp.message;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Data
public class UserRegistrationMessage implements Serializable {
    private String email;
    private String code;
}
