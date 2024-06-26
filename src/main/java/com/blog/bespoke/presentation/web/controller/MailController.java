package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.application.usecase.UserUseCase;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.Envelope;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final UserUseCase userUseCase;

    @Envelope("이메일 인증이 완료되었습니다.")
    @GetMapping("/email-validation")
    public ResponseEntity<UserResponseDto> emailValidation(@RequestParam(name = "code") String code) {
        return ResponseEntity.ok(UserResponseDto.from(userUseCase.emailValidation(code)));
    }
}
