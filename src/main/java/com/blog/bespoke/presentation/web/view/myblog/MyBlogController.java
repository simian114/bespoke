package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class MyBlogController {
    private final UserUseCase userUseCase;
    private final PostUseCase postUseCase;


    /**
     * menu 가 선택 되어있지 않으면, post list
     * menu
     * - profile
     * - post
     * - following
     * - follower
     * (내가 작성한 댓글도 관리하게 할 것인지 결정한기.)
     */
    @GetMapping({"/blog/{nickname}/manage", "/blog/{nickname}/manage/profile"})
    public String myBlog(@PathVariable("nickname") String nickname,
                         @LoginUser User currentUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        // redirect to home and show message

        if (!isOwner(nickname, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "주인장만 접근 가능");
            return "redirect:/home";
        }
        User owner = userUseCase.getUserByNickname(nickname);
        model.addAttribute("owner", owner);
        return "page/myblog/profile";
    }

    /**
     * 정ㄹ려도 query parameter
     * page 는 query parameter
     * pageSize 는 기본값 사용
     * model 에 넣는 값은 pagination 정보와 post 리스트
     */
    @GetMapping("/blog/{nickname}/manage/posts")
    public String postManage(@PathVariable("nickname") String nickname,
                             @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                             @LoginUser User currentUser,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             HtmxRequest htmxRequest) {
        if (!isOwner(nickname, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "주인장만 접근 가능");
            return "redirect:/home";
        }

        // TODO: 정리 필요함
        PostSearchCond postSearchCond = new PostSearchCond();
        postSearchCond.setPage(page);
        postSearchCond.setPageSize(20);
        postSearchCond.setNickname(nickname);

        Page<PostResponseDto> postPage = postUseCase.postSearch(postSearchCond, currentUser);

        User owner = userUseCase.getUserByNickname(nickname);
        model.addAttribute("owner", owner);
        model.addAttribute("posts", postPage.getContent());

        // 페이지네이션 관련된 객체로 하나 만들고 그거 넣으면 페이제네이션 완성되도록 하기.
        model.addAttribute("totalElements", postPage.getTotalElements());
        model.addAttribute("isLast", postPage.isLast());
        model.addAttribute("isFirst", postPage.isFirst());
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("page", page);


        if (htmxRequest.isHtmxRequest()) {

            return "page/myblog/postTable :: .table-container";
        }
        //
        // page 는 url 을 통해..
        // url 로 쏜다.
        return "page/myblog/postTable";
    }

    @GetMapping("/blog/{nickname}/manage/follows")
    public String followManage(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                               @PathVariable("nickname") String nickname,
                               @LoginUser User currentUser
    ) {
        // page
        PostSearchCond postSearchCond = new PostSearchCond();
        postSearchCond.setPage(page);
        postSearchCond.setPageSize(20);
        postSearchCond.setNickname(nickname);
        Page<PostResponseDto> postPage = postUseCase.postSearch(postSearchCond, currentUser);
        return "";
    }

    @GetMapping("/blog/{nickname}/manage/categories")
    public String categoryManage() {
        return "";
    }


    private boolean isOwner(String nickname, User currentUser) {

        if (currentUser == null) {
            return false;
        }
        return nickname.equals(currentUser.getNickname());
    }

}
