package com.blog.bespoke.presentation.web.view;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostUseCase postUseCase;

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
        // TODO: 캐싱
//        response.setHeader("Cache-Control", "max-age=3600, must-revalidate, no-transform");
//        response.setHeader("Pragma", "");
//        response.setHeader("Expires", "");
//        ZonedDateTime now = ZonedDateTime.now();
//        response.setHeader("Last-Modified", now.format(DateTimeFormatter.RFC_1123_DATE_TIME));
//        // 조건부 요청 처리
//        String ifNoneMatch = request.getHeader("If-None-Match");
//        String eTag = UUID.randomUUID().toString();
//        response.setHeader("ETag", eTag);
//        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
//            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
//            return null;
//        }
        return "page/home/home";
    }


    // TODO: cacheing 처리 해야함
    @HxRequest
    @GetMapping("/hx/home/posts")
    // 공통으로 model 에 user 는 넣기
    public String getPostList(@ModelAttribute PostSearchCond cond, @LoginUser User currentUser, Model model) {
        if (cond.getOrderBy() == null) {
            cond.setOrderBy(PostSearchCond.OrderBy.LATEST);
        }
        Page<PostResponseDto> postResponseDtos = postUseCase.postSearch(cond, currentUser);
        List<PostResponseDto> contents = postResponseDtos.getContent();
        long totalElements = postResponseDtos.getTotalElements();

        model.addAttribute("totalElements", totalElements);
        model.addAttribute("hasNextPage", postResponseDtos.hasNext());
        model.addAttribute("posts", contents);
        model.addAttribute("page", postResponseDtos.getPageable().getPageNumber());
        model.addAttribute("me", currentUser);

        return "page/home/home :: .post-list";
    }

}
