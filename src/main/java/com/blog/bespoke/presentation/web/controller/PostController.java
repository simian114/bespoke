package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.usecase.post.PostLikeUseCase;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.post.PostStatusCmd;
import com.blog.bespoke.domain.model.post.PostUpdateCmd;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.Envelope;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostUseCase postUseCase;
    private final PostLikeUseCase postLikeUseCase;

    @PostMapping
    public ResponseEntity<?> write(@Valid @RequestBody PostCreateRequestDto requestDto, @LoginUser User currentUser) {
        return ResponseEntity.ok(postUseCase.write(requestDto, currentUser));
    }

    @PatchMapping("/{postId}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long postId,
                                          @RequestBody PostStatusCmd cmd,
                                          @LoginUser User currentUser) {
        return ResponseEntity.ok(postUseCase.changeStatus(postId, cmd, currentUser));
    }

    @PutMapping("/{postId}/edit")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostUpdateCmd postUpdateCmd, @LoginUser User currentUser) {
        return ResponseEntity.ok(postUseCase.updatePost(postId, postUpdateCmd, currentUser));
    }

    @GetMapping
    public ResponseEntity<?> search(@ModelAttribute PostSearchCond cond, @LoginUser User currentUser) {
        return ResponseEntity.ok(postUseCase.postSearch(cond, currentUser));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> showPostById(@PathVariable Long postId, @LoginUser User currentUser) {
        return ResponseEntity.ok(postUseCase.showPostById(postId, currentUser));
    }

    @Envelope("삭제했습니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteById(@PathVariable Long postId, @LoginUser User currentUser) {
        postUseCase.deletePost(postId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @Envelope("게시글 좋아요를 눌렀습니다.")
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @LoginUser User currentUser) {
        return ResponseEntity.ok(postLikeUseCase.likePost(postId, currentUser));
    }

    @Envelope("게시글 좋아요를 취소했습니다.")
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<?> cancelLikePost(@PathVariable Long postId, @LoginUser User currentUser) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(postLikeUseCase.cancelLikePost(postId, currentUser));
    }

}
