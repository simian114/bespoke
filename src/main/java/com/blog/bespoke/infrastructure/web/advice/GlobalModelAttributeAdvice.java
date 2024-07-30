package com.blog.bespoke.infrastructure.web.advice;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Component
@ControllerAdvice
public class GlobalModelAttributeAdvice {
    private String randomValue;

    @PostConstruct
    public void init() {
        randomValue = UUID.randomUUID().toString().substring(0, 8);
    }

    @ModelAttribute
    public void getRandomValue(Model model,
                               @LoginUser User currentUser) {
        model.addAttribute("randomValue", randomValue);
        model.addAttribute("me", currentUser);
    }

}
