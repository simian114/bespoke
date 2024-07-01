package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.usecase.PostUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostUseCase postUseCase;

    @PostMapping
    public ResponseEntity<?> write(@Valid @RequestBody PostCreateRequestDto requestDto, @LoginUser User currentUser) {
        return ResponseEntity.ok(postUseCase.write(requestDto, currentUser));
    }
}