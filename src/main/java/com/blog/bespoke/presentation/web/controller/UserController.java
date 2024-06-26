package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.usecase.UserUseCase;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.Envelope;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;

    @Envelope("이메일 인증을 해주세요!")
    @PostMapping
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        userUseCase.signup(requestDto);
        return ResponseEntity.ok().build();
    }
}
