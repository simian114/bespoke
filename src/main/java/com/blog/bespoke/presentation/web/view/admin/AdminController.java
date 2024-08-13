package com.blog.bespoke.presentation.web.view.admin;

import com.blog.bespoke.application.dto.request.postSearch.PostSearchForAdmin;
import com.blog.bespoke.application.dto.request.userSearch.UserSearchForAdmin;
import com.blog.bespoke.application.dto.response.PostSearchResponseDto;
import com.blog.bespoke.application.dto.response.UserSearchResponseDto;
import com.blog.bespoke.application.usecase.post.PostSearchUseCase;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserSearchUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * spring security config 를 통해 어드민 유저가 아닌 경우 해당 경로로 들어오지 못하도록 세팅
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PostUseCase postUseCase;
    private final UserUseCase userUseCase;
    private final PostSearchUseCase postSearchUseCase;
    private final UserSearchUseCase userSearchUseCase;

    @ModelAttribute
    void initModelAttribute(Model model,
                            @LoginUser User currentUser) {
        model.addAttribute("me", currentUser);
    }

    /**
     * 디폴트는 게시글 관리
     */
    @GetMapping({"", "/", "/post"})
    public HtmxResponse postManage(@ModelAttribute PostSearchForAdmin requestDto,
                                   @LoginUser User currentUser,
                                   Model model) {
        PostSearchResponseDto res = postSearchUseCase.postSearch(requestDto, currentUser);
        model.addAttribute("posts", res.getContent());
        model.addAttribute("cond", requestDto);
        // NOTE: temp save 는 제외하고
        model.addAttribute("statuses", Post.Status.getStatusesWithoutTempSave());

        // pagination modela

        model.addAttribute("totalElements", res.getTotalElements());
        model.addAttribute("isLast", !res.hasNext());
        model.addAttribute("isFirst", !res.hasPrevious());
        model.addAttribute("totalPages", res.getTotalPage());
        model.addAttribute("page", res.getPage());

        return HtmxResponse.builder()
                .view("page/admin/post/post")
                .build();
    }

    /**
     * 유저 관리 페이지
     */
    @GetMapping("/user")
    public HtmxResponse userManage(@ModelAttribute UserSearchForAdmin requestDto,
                                   Model model,
                                   @LoginUser User currentUser
                                   ) {
        UserSearchResponseDto res = userSearchUseCase.userSearch(requestDto, currentUser);

        model.addAttribute("users", res.getContent());
        model.addAttribute("cond", requestDto);
        model.addAttribute("statuses", Post.Status.getStatusesWithoutTempSave());

        model.addAttribute("totalElements", res.getTotalElements());
        model.addAttribute("isLast", !res.hasNext());
        model.addAttribute("isFirst", !res.hasPrevious());
        model.addAttribute("totalPages", res.getTotalPage());
        model.addAttribute("page", res.getPage());

        return HtmxResponse.builder()
                .view("page/admin/user/user")
                .build();
    }

    /**
     * 배너 관리 페이지 (서비스를 먼저 개발해야함)
     */
    @GetMapping("/banner")
    public HtmxResponse bannerManage() {
        return HtmxResponse.builder().build();
    }

    /**
     * 신고 관리 페이지
     * 신고 대상은 아래와 같다.
     * - 유저 신고 (유저는 적절치 않은 닉네임)
     * - 게시글 (나쁜 게시글)
     * - 댓글 (나쁜 댓글)
     */
    @GetMapping("/report")
    public HtmxResponse reportManage() {
        return HtmxResponse.builder().build();
    }

}
