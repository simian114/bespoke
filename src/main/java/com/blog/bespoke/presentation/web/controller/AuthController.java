package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.application.dto.request.LoginRequestDto;
import com.blog.bespoke.application.dto.request.ReIssueTokenRequestDto;
import com.blog.bespoke.application.usecase.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(authUseCase.login(requestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reIssueToken(@Valid @RequestBody ReIssueTokenRequestDto requestDto) {
        return ResponseEntity.ok(authUseCase.reIssueToken(requestDto));
    }

    @GetMapping("/test")
    public String test() {
        return "zz";
    }

}
