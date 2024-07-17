package com.blog.bespoke.presentation.web.view.blog;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostUseCase postUseCase;
    private final UserUseCase userUseCase;

    @GetMapping("/blog/posts/{postId}")
    public String postDetailPage(@PathVariable("postId") Long postId,
                                 @LoginUser User currentUser,
                                 Model model) {
        PostResponseDto post = postUseCase.showPostById(postId, currentUser);
        UserResponseDto owner = userUseCase.getUserWithCategoryByNickname(post.getAuthor().getNickname());

        model.addAttribute("me", currentUser);
        model.addAttribute("isOwner", currentUser != null && currentUser.getNickname().equals(owner.getNickname()));
        model.addAttribute("post", post);
        model.addAttribute("owner", owner);

        // TODO: 댓글...
        // TODO: floating TOC 만들기. 모바일 환경에서는 최상단에 details / summary 형태로 구현
        return "page/post/post";
    }
}
