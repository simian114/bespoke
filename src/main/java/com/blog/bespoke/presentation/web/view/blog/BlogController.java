package com.blog.bespoke.presentation.web.view.blog;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.service.UserService;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final UserUseCase userUseCase;
    private final PostUseCase postUseCase;
    private final UserService userService;

    // NOTE: /{nickname}/{category}
    @GetMapping("/{nickname}")
    public String blogHomePage(@PathVariable("nickname") String nickname,
                           @LoginUser User currentUser,
                           Model model) {
        User owner = userUseCase.getUserByNickname(nickname);

        model.addAttribute("owner", owner);
        model.addAttribute("me", currentUser);
        // category ..
        log.info(nickname);
        // hx data...
        return "page/blog/blog";
    }

    @GetMapping("/{nickname}/{category}")
    public String blogCategory(@PathVariable("nickname") String nickname,
                               @LoginUser User currentUser,
                               Model model) {
        // TODO: category
        User owner = userUseCase.getUserByNickname(nickname);

        model.addAttribute("owner", owner);
        model.addAttribute("me", currentUser);

        return "page/blog/blog";
    }

    @GetMapping("/hx/{nickname}/posts")
    public String getUserPosts(@PathVariable("nickname") String nickname,
                               @ModelAttribute PostSearchCond cond,
                               @LoginUser User currentUser,
                               Model model) {

        User owner = userUseCase.getUserByNickname(nickname);
        cond.setNickname(nickname);
        Page<PostResponseDto> posts = postUseCase.postSearch(cond, currentUser);
        model.addAttribute("totalElements", posts.getTotalElements());
        model.addAttribute("hasNextPage", posts.hasNext());
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("page", posts.getPageable().getPageNumber());
        model.addAttribute("owner", owner);

        model.addAttribute("me", currentUser);

        return "page/blog/blog :: post-list";
    }

    private boolean isOwner(String nickname, User currentUser) {
        if (currentUser == null) {
            return false;
        }
        return nickname.equals(currentUser.getNickname());
    }

}
