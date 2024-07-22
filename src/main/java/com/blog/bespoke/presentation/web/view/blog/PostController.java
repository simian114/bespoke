package com.blog.bespoke.presentation.web.view.blog;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.usecase.post.PostLikeUseCase;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostUseCase postUseCase;
    private final PostLikeUseCase postLikeUseCase;

    @GetMapping("/blog/posts/{postId}")
    public String postDetailPage(@PathVariable("postId") Long postId,
                                 @LoginUser User currentUser,
                                 Model model) {
        PostResponseDto post = postUseCase.showPostById(postId, currentUser);

        model.addAttribute("me", currentUser);
        model.addAttribute("isOwner", currentUser != null && currentUser.getNickname().equals(post.getAuthor().getNickname()));
        model.addAttribute("post", post);
        model.addAttribute("owner", post.getAuthor());

        // TODO: 댓글...
        // TODO: floating TOC 만들기. 모바일 환경에서는 최상단에 details / summary 형태로 구현
        return "page/post/post";
    }

    @HxRequest
    @PostMapping("/blog/posts/{postId}/like")
    public String postLike(@PathVariable("postId") Long postId,
                           @LoginUser User currentuser,
                           Model model) {
        PostResponseDto post = postLikeUseCase.likePost(postId, currentuser);

        model.addAttribute("post", post);
        return "page/post/post :: like";
    }

    @HxRequest
    @DeleteMapping("/blog/posts/{postId}/like")
    public HtmxResponse cancelPostLike(@PathVariable("postId") Long postId,
                                       @LoginUser User currentuser,
                                       Model model) {
        PostResponseDto post = postLikeUseCase.cancelLikePost(postId, currentuser);
        model.addAttribute("post", post);
        return HtmxResponse.builder()
                .view("page/post/post :: cancel-like")
                .build();
    }
}

