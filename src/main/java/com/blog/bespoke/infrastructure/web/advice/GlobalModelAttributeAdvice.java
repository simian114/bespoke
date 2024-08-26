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

    /**
     * randomValue: static resource 의 캐싱을 위한 값. 재실행 될 때 마다 새로운 값이 부여됨 -> 캐싱이 초기화됨
     * me: 현재 로그인 한 유저
     * isAdmin / isAdvertiser: 권한 체크
     */
    @ModelAttribute
    public void setGlobalModelAttribute(Model model,
                                        @LoginUser User currentUser) {
        model.addAttribute("randomValue", randomValue);
        model.addAttribute("me", currentUser);
        if (currentUser != null) {
            model.addAttribute("isAdmin", currentUser.isAdmin());
            model.addAttribute("isAdvertiser", currentUser.isAdvertiser());
        }
    }


}
