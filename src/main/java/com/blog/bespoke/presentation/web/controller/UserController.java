package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.application.dto.request.CategoryCreateRequestDto;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.user.UserCategoryUseCase;
import com.blog.bespoke.application.usecase.user.UserFollowUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.CategoryUpdateCmd;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.Envelope;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;
    private final UserFollowUseCase userFollowUseCase;
    private final UserCategoryUseCase userCategoryUseCase;

    @Envelope("이메일 인증을 해주세요!")
    @PostMapping
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        userUseCase.signup(requestDto);
        return ResponseEntity.ok().build();
    }

    // follow. 예외 처리
    @Envelope("팔로우 성공")
    @PostMapping("/{id}/follow")
    public ResponseEntity<?> follow(@PathVariable Long id, @LoginUser User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userFollowUseCase.follow(id, currentUser));
    }

    @Envelope("팔로우 취소")
    @DeleteMapping("/{id}/follow")
    public ResponseEntity<?> unfollow(@PathVariable Long id, @LoginUser User currentUser) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userFollowUseCase.unfollow(id, currentUser));
    }

//    @PutMapping("/{id}/category/{categoryId}")
//    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
//                                            @PathVariable("categoryId") Long categoryId,
//                                            @RequestBody CategoryUpdateCmd cmd,
//                                            @LoginUser User currentUser
//    ) {
//        userCategoryUseCase.updateCategory(categoryId, cmd, currentUser);
//        return ResponseEntity.ok("");
//    }
}
