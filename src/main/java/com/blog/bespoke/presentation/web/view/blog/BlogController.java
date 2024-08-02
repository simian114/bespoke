package com.blog.bespoke.presentation.web.view.blog;

import com.blog.bespoke.application.dto.request.postSearch.PostSearchForBlogRequestDto;
import com.blog.bespoke.application.dto.response.PostSearchResponseDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.post.PostSearchUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * me: 로그인 한 유저의 jwt 정보
 * owner: 블로그 주인
 * isOwner: 블로그 주인인지 확인
 */
@Slf4j
@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final UserUseCase userUseCase;
    private final PostSearchUseCase postSearchUseCase;

    // NOTE: /{nickname}/{category}
    @GetMapping("/{nickname}")
    public String blogHomePage(@PathVariable("nickname") String nickname,
                               @LoginUser User currentUser,
                               Model model) {
        // getUserForBlog -> profile & category
        UserResponseDto owner = userUseCase.getUserForUserHome(nickname);

        model.addAttribute("me", currentUser);
        model.addAttribute("owner", owner);
        model.addAttribute("isOwner", isOwner(nickname, currentUser));

        return "page/blog/blog";
    }

    // 카테고리 페이지
    @GetMapping("/{nickname}/category/{categoryUrl}")
    public String blogCategory(@PathVariable("nickname") String nickname,
                               @PathVariable("categoryUrl") String categoryUrl,
                               @LoginUser User currentUser,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        // TODO: category
        // NOTE: userProfile 과 userCount 를 한번에 join 해오는 방법은 없을까?
        UserResponseDto owner = userUseCase.getUserForUserHome(nickname);
        // selected category
        UserResponseDto.CategoryResponseDto selectedCategory = owner.getCategories().stream()
                .filter(c -> categoryUrl.equals(c.getUrl()))
                .findFirst()
                .orElse(null);
        if (selectedCategory == null) {
            redirectAttributes.addFlashAttribute("error", "no category");
            return String.format("redirect:/blog/%s", nickname);
        }

        model.addAttribute("me", currentUser);
        model.addAttribute("owner", owner);
        model.addAttribute("isOwner", owner.getId().equals(currentUser == null ? null : currentUser.getId()));
        model.addAttribute("category", selectedCategory);

        return "page/blog/category";
    }

    // NOTE: 나중에..
    @HxRequest
    @GetMapping("/posts")
    public String getUserPosts(@ModelAttribute PostSearchForBlogRequestDto requestDto,
                               @LoginUser User currentUser,
                               Model model) {
        PostSearchResponseDto posts = postSearchUseCase.postSearch(requestDto, currentUser);

        model.addAttribute("isEmpty", posts.isEmpty());
        model.addAttribute("totalElements", posts.getTotalElements());
        model.addAttribute("hasNextPage", posts.hasNext());
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("page", posts.getPage() + 1);

        return "page/blog/category :: .post-list";
    }

    private boolean isOwner(String nickname, User currentUser) {
        if (currentUser == null) {
            return false;
        }
        return nickname.equals(currentUser.getNickname());
    }

}
