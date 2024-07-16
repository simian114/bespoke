package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.request.CategoryCreateRequestDto;
import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.dto.request.PostUpdateRequestDto;
import com.blog.bespoke.application.dto.request.UserUpdateRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserCategoryUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.post.PostStatusCmd;
import com.blog.bespoke.domain.model.user.CategoryUpdateCmd;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class MyBlogController {
    private final UserUseCase userUseCase;
    private final PostUseCase postUseCase;
    private final UserCategoryUseCase userCategoryUseCase;


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

        UserResponseDto owner = userUseCase.getUserByNickname(nickname);
        UserUpdateRequestDto user = UserUpdateRequestDto.builder()
                .name(owner.getName())
                .introduce(owner.getUserProfile().getIntroduce())
                .build();
        model.addAttribute("owner", owner);
        model.addAttribute("user", user);
        return "page/myblog/profile";
    }

    @PostMapping({"/blog/{nickname}/manage", "/blog/{nickname}/manage/profile"})
    public String updateProfile(@Valid @ModelAttribute("user") UserUpdateRequestDto requestDto,
                                BindingResult bindingResult,
                                @LoginUser User currentUser,
                                Model model,
                                @PathVariable("nickname") String nickname,
                                RedirectAttributes redirectAttributes) {
        if (!isOwner(nickname, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "주인장만 접근 가능");
            return "redirect:/home";
        }
        if (bindingResult.hasErrors()) {
            UserResponseDto owner = userUseCase.getUserByNickname(nickname);
            model.addAttribute("owner", owner);
            return "page/myblog/profile";
        }
        // update user
        userUseCase.updateUser(requestDto, currentUser.getId());
        return String.format("redirect:/blog/%s", nickname);
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
        postSearchCond.setManage(true);
        postSearchCond.setNickname(nickname);

        Page<PostResponseDto> postPage = postUseCase.postSearch(postSearchCond, currentUser);

        UserResponseDto owner = userUseCase.getUserByNickname(nickname);
        model.addAttribute("owner", owner);
        model.addAttribute("posts", postPage.getContent());

        // 페이지네이션 관련된 객체로 하나 만들고 그거 넣으면 페이제네이션 완성되도록 하기.
        model.addAttribute("totalElements", postPage.getTotalElements());
        model.addAttribute("isLast", postPage.isLast());
        model.addAttribute("isFirst", postPage.isFirst());
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("page", page);

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

    /**
     * 카테고리 리스트
     */
    @GetMapping("/blog/{nickname}/manage/categories")
    public String categoryManage(@PathVariable("nickname") String nickname,
                                 @LoginUser User currentUser,
                                 Model model) {
        // page
        UserResponseDto me = userUseCase.getUserByNickname(nickname);
        if (!isOwner(nickname, currentUser)) {
            return "redirect:/";
        }
        model.addAttribute("nickname", nickname);
        model.addAttribute("owner", me);

        UserResponseDto userWithCategory = userCategoryUseCase.getUserWithCategory(currentUser.getId());
        model.addAttribute("categories", userWithCategory.getCategories());
        return "page/myblog/categoryTable";
    }

    @GetMapping("/blog/{nickname}/manage/categories/new")
    public String categoryCreatePage(@PathVariable("nickname") String nickname,
                                     @ModelAttribute("category") CategoryCreateRequestDto requestDto,
                                     @LoginUser User currentUser,
                                     Model model) {
        UserResponseDto me = userUseCase.getUserByNickname(nickname);
        if (!isOwner(nickname, currentUser)) {
            return "redirect:/";
        }
        model.addAttribute("nickname", nickname);
        model.addAttribute("owner", me);
        model.addAttribute("action", String.format("/blog/%s/manage/categories", nickname));
        return "page/myblog/categoryForm";
    }

    @GetMapping("/blog/{nickname}/manage/categories/{categoryId}")
    public String editCategoryPage(@PathVariable("nickname") String nickname,
                                   @PathVariable("categoryId") Long categoryId,
                                   @LoginUser User currentUser,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        UserResponseDto me = userUseCase.getUserByNickname(nickname);
        if (!isOwner(nickname, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "권한없음");
            return "redirect:/";
        }
        UserResponseDto userWithCategory = userCategoryUseCase.getUserWithCategory(currentUser.getId());
        UserResponseDto.CategoryResponseDto category = userWithCategory.getCategories().stream().filter(c -> c.getId().equals(categoryId))
                .findAny().orElseThrow();

        model.addAttribute("nickname", nickname);
        model.addAttribute("owner", me);
        model.addAttribute("category", category);
        model.addAttribute("action", String.format("/blog/%s/manage/categories/%d", nickname, categoryId));
        return "page/myblog/categoryForm";
    }

    @PostMapping("/blog/{nickname}/manage/categories/{categoryId}")
    public String categoryUpdate(@PathVariable("nickname") String nickname,
                                 @PathVariable("categoryId") Long categoryId,
                                 @LoginUser User currentUser,
                                 @Valid @ModelAttribute("category") CategoryUpdateCmd requestDto,
                                 BindingResult bindingResult) {
        UserResponseDto me = userUseCase.getUserByNickname(nickname);
        if (!isOwner(nickname, currentUser)) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "page/myblog/categoryForm";
        }
        userCategoryUseCase.updateCategory(categoryId, requestDto, me.getId());
        return String.format("redirect:/blog/%s/manage/categories", nickname);
    }


    @PostMapping("/blog/{nickname}/manage/categories")
    public String categoryCreate(@PathVariable("nickname") String nickname,
                                 @LoginUser User currentUser,
                                 @Valid @ModelAttribute("category") CategoryCreateRequestDto requestDto,
                                 BindingResult bindingResult,
                                 Model model) {
        UserResponseDto me = userUseCase.getUserByNickname(nickname);
        if (!isOwner(nickname, currentUser)) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "page/myblog/categoryForm";
        }
        userCategoryUseCase.createCategory(requestDto, me.getId());
        return String.format("redirect:/blog/%s/manage/categories", nickname);
    }

    @ResponseBody
    @DeleteMapping("/blog/{nickname}/manage/categories/{categoryId}")
    public String deleteCategory(@PathVariable("nickname") String nickname,
                                 @PathVariable("categoryId") Long categoryId,
                                 @LoginUser User currentUser,
                                 Model model) {
        UserResponseDto me = userUseCase.getUserByNickname(nickname);
        if (!isOwner(nickname, currentUser)) {
            return "redirect:/";
        }
        userCategoryUseCase.deleteCategory(categoryId, currentUser);
        model.addAttribute("nickname", nickname);
        model.addAttribute("owner", me);
        return "";
    }

    @GetMapping("/blog/{nickname}/manage/posts/new")
    public String createPostPage(@PathVariable("nickname") String nickname,
                                 @LoginUser User currentUser,
                                 Model model) {
        UserResponseDto me = userUseCase.getUserForPostWrite(nickname);
        if (!isOwner(nickname, currentUser)) {
            return "redirect:/";
        }
        model.addAttribute("post", PostCreateRequestDto.builder().content("Write your stories").build());

        model.addAttribute("nickname", nickname);
        model.addAttribute("owner", me);
        model.addAttribute("categories", me.getCategories());

        model.addAttribute("action", String.format("/blog/%s/manage/posts", nickname));
        return "page/myblog/postEditor";
    }

    // NOTE: hx?
    @PostMapping("/blog/{nickname}/manage/posts")
    public String createPost(@PathVariable("nickname") String nickname,
                             @LoginUser User currentUser,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             @Valid @ModelAttribute(name = "post") PostCreateRequestDto requestDto,
                             BindingResult bindingResult) {
        UserResponseDto me = userUseCase.getUserForPostWrite(nickname);
        model.addAttribute("owner", me);
        model.addAttribute("nickname", nickname);
        model.addAttribute("categories", me.getCategories());
        model.addAttribute("action", String.format("/blog/%s/manage/posts", nickname));
        if (bindingResult.hasErrors()) {
            return "page/myblog/postEditor";
        }

        // category
        PostResponseDto postDto = postUseCase.write(requestDto, currentUser);
        redirectAttributes.addFlashAttribute("success", "post created");

        return String.format("redirect:/blog/%s/manage/posts", nickname);
    }

    @HxRequest
    @PatchMapping("/blog/{nickname}/manage/posts/{postId}/status/{status}")
    public void changeStatus(@PathVariable("nickname") String nickname,
                             @PathVariable("postId") Long postId,
                             @PathVariable("status") Post.Status status,
                             @LoginUser User currentUser) {

        // TODO: 하나 집어서 수정할 수 있게 변경
        PostStatusCmd cmd = new PostStatusCmd();
        cmd.setStatus(status);
        postUseCase.changeStatus(postId, cmd, currentUser);
    }

    /**
     * 삭제 실패한 경우 처리해야함.
     * 삭제 된 게시글은 리스트에 보이면 안됨 -> search 에서 삭제 된 게시글은 알아서 걸러져야 한다.
     */
    @HxRequest
    @DeleteMapping("/blog/{nickname}/manage/posts/{postId}")
    @ResponseBody
    public String deletePost(@PathVariable("nickname") String nickname,
                             @PathVariable("postId") Long postId,
                             @LoginUser User currentUser) {
        try {
            postUseCase.deletePost(postId, currentUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }


    /**
     * 게시글 수정페이지
     */
    @GetMapping("/blog/{nickname}/manage/posts/{postId}/edit")
    public String editPostPage(@PathVariable("nickname") String nickname,
                               @PathVariable("postId") Long postId,
                               @LoginUser User currentUser,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (!isOwner(nickname, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "권한없음");
            return "redirect:/";
        }
        // post -> postCreate
        UserResponseDto me = userUseCase.getUserForPostWrite(nickname);
        // User me = userUseCase.getUserByNickname(nickname);
        Post post = postUseCase.getPostById(postId);
        PostCreateRequestDto dto = PostCreateRequestDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .status(post.getStatus())
                .build();
        model.addAttribute("post", dto);
        model.addAttribute("nickname", nickname);
        model.addAttribute("owner", me);
        model.addAttribute("categories", me.getCategories());
        model.addAttribute("action", String.format("/blog/%s/manage/posts/%d", nickname, postId));
        return "page/myblog/postEditor";
    }

    // 수정 api
    @PostMapping("/blog/{nickname}/manage/posts/{postId}")
    public String updatePost(@PathVariable("nickname") String nickname,
                             @PathVariable("postId") Long postId,
                             @LoginUser User currentUser,
                             @Valid @ModelAttribute("post") PostUpdateRequestDto requestDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (!isOwner(nickname, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "권한없음");
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "page/myblog/postEditor";
        }
        // TODO: update
        postUseCase.updatePost(postId, requestDto, currentUser);
        return String.format("redirect:/blog/%s/manage/posts", nickname);
    }

    private boolean isOwner(String nickname, User currentUser) {

        if (currentUser == null) {
            return false;
        }
        return nickname.equals(currentUser.getNickname());
    }

}
