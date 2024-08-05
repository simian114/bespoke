package com.blog.bespoke.presentation.web.view;

import com.blog.bespoke.application.dto.request.postSearch.PostSearchForMainHomeRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.PostSearchResponseDto;
import com.blog.bespoke.application.usecase.post.PostSearchUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostSearchUseCase postSearchUseCase;

    /**
     * NOTE
     * 홈페이지는 스태틱하게 가져가도록. 현재 헤더에서 security 의 isAuthenticated를 사용중인데
     * 그 부분도 동적으로 데이터 가져오게 변경
     * 이후에 홈페이지는 캐싱 후 사용자에게 제공
     * 1. http 캐싱
     * 2. 레디스 캐싱
     * <p>
     * TODO: spring security 의 http.headers().cacheControl 에 대해 알아봐야함
     */
    @GetMapping({"", "/",})
    public String home(HttpServletRequest request, HttpServletResponse response, @LoginUser User currentUser, Model model) {
        model.addAttribute("me", currentUser);

        // NOTE: post list
        return "page/home/home";
    }


    // TODO: cacheing 처리 해야함
    @HxRequest
    @GetMapping("/hx/home/posts")
    // 공통으로 model 에 user 는 넣기
    public String getPostList(@ModelAttribute PostSearchForMainHomeRequestDto requestDto,
                              @LoginUser User currentUser,
                              Model model) {
        PostSearchResponseDto posts = postSearchUseCase.postSearch(requestDto, currentUser);

        List<PostResponseDto> contents = posts.getContent();
        long totalElements = posts.getTotalElements();

        model.addAttribute("totalElements", totalElements);
        model.addAttribute("hasNextPage", posts.hasNext());
        model.addAttribute("posts", contents);
        model.addAttribute("page", posts.getPage());
        model.addAttribute("me", currentUser);

        return "page/home/home :: .post-list";
    }

}
