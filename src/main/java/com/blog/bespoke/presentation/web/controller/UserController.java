package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.usecase.UserUseCase;
import com.blog.bespoke.presentation.web.dto.response.UserResponseDto;
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

    @PostMapping
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        return ResponseEntity.ok(UserResponseDto.from(userUseCase.signup(requestDto)));
    }
}
