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
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
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

    @ModelAttribute
    public String handleCommonAttribute(@LoginUser User currentUser,
                                        Model model) {
        model.addAttribute("me", currentUser);
        model.addAttribute("nickname", currentUser.getNickname());
        return "";
    }


    /**
     * menu 가 선택 되어있지 않으면, post list
     * menu
     * - profile
     * - post
     * - following
     * - follower
     * (내가 작성한 댓글도 관리하게 할 것인지 결정한기.)
     */
    @GetMapping({"/blog/manage", "/blog/manage/profile"})
    public String myBlog(@LoginUser User currentUser,
                         Model model) {
        UserResponseDto owner = userUseCase.getUserForProfileEdit(currentUser.getId());
        UserUpdateRequestDto user = UserUpdateRequestDto.builder()
                .name(owner.getName())
                .introduce(owner.getUserProfile().getIntroduce())
                .build();
        model.addAttribute("user", user);
        return "page/myblog/profile";
    }

    /**
     * 프로필 수정 api
     */
    @HxRequest
    @PostMapping({"/blog/manage", "/blog/manage/profile"})
    public HtmxResponse updateProfile(@Valid @ModelAttribute("user") UserUpdateRequestDto requestDto,
                                      BindingResult bindingResult,
                                      @LoginUser User currentUser,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            UserResponseDto owner = userUseCase.getUserById(currentUser.getId());
            model.addAttribute("owner", owner);
            return HtmxResponse.builder()
                    .view("page/myblog/profile")
                    .build();
        }
        userUseCase.updateUser(requestDto, currentUser.getId());
        return HtmxResponse.builder()
                .redirect(String.format("/blog/%s", currentUser.getNickname()))
                .build();
    }

    /**
     * 정ㄹ려도 query parameter
     * page 는 query parameter
     * pageSize 는 기본값 사용
     * model 에 넣는 값은 pagination 정보와 post 리스트
     */
    @GetMapping("/blog/manage/posts")
    public String postManage(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @LoginUser User currentUser,
            Model model) {

        // TODO: 정리 필요함
        PostSearchCond postSearchCond = new PostSearchCond();
        postSearchCond.setPage(page);
        postSearchCond.setPageSize(20);
        postSearchCond.setManage(true);
        postSearchCond.setNickname(currentUser.getNickname());

        Page<PostResponseDto> postPage = postUseCase.postSearch(postSearchCond, currentUser);

        UserResponseDto owner = userUseCase.getUserById(currentUser.getId());
        model.addAttribute("owner", owner);
        model.addAttribute("me", owner);
        model.addAttribute("posts", postPage.getContent());

        // 페이지네이션 관련된 객체로 하나 만들고 그거 넣으면 페이제네이션 완성되도록 하기.
        model.addAttribute("totalElements", postPage.getTotalElements());
        model.addAttribute("isLast", postPage.isLast());
        model.addAttribute("isFirst", postPage.isFirst());
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("page", page);

        return "page/myblog/postTable";
    }

    @GetMapping("/blog/manage/follows")
    public String followManage(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                               @LoginUser User currentUser
    ) {
        // page
        PostSearchCond postSearchCond = new PostSearchCond();
        postSearchCond.setPage(page);
        postSearchCond.setPageSize(20);
        postSearchCond.setNickname(currentUser.getNickname());
        Page<PostResponseDto> postPage = postUseCase.postSearch(postSearchCond, currentUser);
        return "";
    }

    /**
     * 카테고리 리스트
     */
    @GetMapping("/blog/manage/categories")
    public String categoryManage(@LoginUser User currentUser,
                                 Model model) {
        UserResponseDto me = userUseCase.getUserById(currentUser.getId());
        model.addAttribute("owner", me);

        UserResponseDto userWithCategory = userCategoryUseCase.getUserWithCategory(currentUser.getId());
        model.addAttribute("categories", userWithCategory.getCategories());
        return "page/myblog/categoryTable";
    }

    @GetMapping("/blog/manage/categories/new")
    public String categoryCreatePage(@ModelAttribute("category") CategoryCreateRequestDto requestDto,
                                     @LoginUser User currentUser,
                                     Model model) {
        UserResponseDto me = userUseCase.getUserById(currentUser.getId());
        model.addAttribute("owner", me);
        model.addAttribute("action", "/blog/manage/categories");
        return "page/myblog/categoryForm";
    }

    @GetMapping("/blog/manage/categories/{categoryId}")
    public String editCategoryPage(@PathVariable("categoryId") Long categoryId,
                                   @LoginUser User currentUser,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        UserResponseDto me = userUseCase.getUserById(currentUser.getId());
        UserResponseDto userWithCategory = userCategoryUseCase.getUserWithCategory(currentUser.getId());
        UserResponseDto.CategoryResponseDto category = userWithCategory.getCategories().stream().filter(c -> c.getId().equals(categoryId))
                .findAny().orElseThrow();

        model.addAttribute("owner", me);
        model.addAttribute("category", category);
        model.addAttribute("action", String.format("/blog/manage/categories/%d", categoryId));
        return "page/myblog/categoryForm";
    }

    @PostMapping("/blog/manage/categories/{categoryId}")
    public String categoryUpdate(
            @PathVariable("categoryId") Long categoryId,
            @LoginUser User currentUser,
            @Valid @ModelAttribute("category") CategoryUpdateCmd requestDto,
            BindingResult bindingResult) {
        UserResponseDto me = userUseCase.getUserById(currentUser.getId());
        if (bindingResult.hasErrors()) {
            return "page/myblog/categoryForm";
        }
        userCategoryUseCase.updateCategory(categoryId, requestDto, me.getId());
        return "redirect:/blog/manage/categories";
    }


    @PostMapping("/blog/manage/categories")
    public String categoryCreate(@LoginUser User currentUser,
                                 @Valid @ModelAttribute("category") CategoryCreateRequestDto requestDto,
                                 BindingResult bindingResult) {
        UserResponseDto me = userUseCase.getUserById(currentUser.getId());
        if (bindingResult.hasErrors()) {
            return "page/myblog/categoryForm";
        }
        userCategoryUseCase.createCategory(requestDto, me.getId());
        return "redirect:/blog/manage/categories";
    }

    @ResponseBody
    @DeleteMapping("/blog/manage/categories/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId,
                                 @LoginUser User currentUser,
                                 Model model) {
        UserResponseDto me = userUseCase.getUserById(currentUser.getId());
        userCategoryUseCase.deleteCategory(categoryId, currentUser);
        model.addAttribute("owner", me);
        return "";
    }

    /**
     * 게시글 생성 페이지
     */
    @GetMapping("/blog/manage/posts/new")
    public String createPostPage(@LoginUser User currentUser,
                                 Model model) {
        UserResponseDto me = userUseCase.getUserForPostWrite(currentUser.getId());
        model.addAttribute("post", PostCreateRequestDto.builder().content("Write your stories").build());

        model.addAttribute("owner", me);
        model.addAttribute("categories", me.getCategories());

        model.addAttribute("action", "/blog/manage/posts");
        return "page/myblog/postEditor";
    }

    /**
     * 게시글 생성 api
     */
    @HxRequest
    @PostMapping("/blog/manage/posts")
    public HtmxResponse createPost(@LoginUser User currentUser,
                                   Model model,
                                   RedirectAttributes redirectAttributes,
                                   @Valid @ModelAttribute(name = "post") PostCreateRequestDto requestDto,
                                   BindingResult bindingResult) {
        UserResponseDto me = userUseCase.getUserForPostWrite(currentUser.getId());
        model.addAttribute("owner", me);
        model.addAttribute("categories", me.getCategories());
        model.addAttribute("action", "/blog/manage/posts");
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder()
                    .preventHistoryUpdate()
                    .view("page/myblog/postEditor")
                    .build();
        }

        // category
        PostResponseDto postDto = postUseCase.write(requestDto, currentUser);
        redirectAttributes.addFlashAttribute("success", "post created");

        return HtmxResponse.builder()
                .redirect("/blog/manage/posts")
                .build();
    }

    @HxRequest
    @PatchMapping("/blog/manage/posts/{postId}/status/{status}")
    public HtmxResponse changeStatus(@PathVariable("postId") Long postId,
                                     @PathVariable("status") Post.Status status,
                                     @LoginUser User currentUser,
                                     Model model) {

        // TODO: 하나 집어서 수정할 수 있게 변경
        PostStatusCmd cmd = new PostStatusCmd();
        cmd.setStatus(status);
        PostResponseDto postResponseDto = postUseCase.changeStatus(postId, cmd, currentUser);
        model.addAttribute("post", postResponseDto);

        return HtmxResponse.builder()
                .view("page/myblog/postTable :: post-item-row")
                .build();
    }

    /**
     * 삭제 실패한 경우 처리해야함.
     * 삭제 된 게시글은 리스트에 보이면 안됨 -> search 에서 삭제 된 게시글은 알아서 걸러져야 한다.
     */
    @HxRequest
    @DeleteMapping("/blog/manage/posts/{postId}")
    @ResponseBody
    public String deletePost(@PathVariable("postId") Long postId,
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
    @GetMapping("/blog/manage/posts/{postId}/edit")
    public String editPostPage(@PathVariable("postId") Long postId,
                               @LoginUser User currentUser,
                               Model model) {
        // post -> postCreate
        UserResponseDto me = userUseCase.getUserForPostWrite(currentUser.getId());
        PostResponseDto post = postUseCase.getPostForEdit(postId);
        PostCreateRequestDto dto = PostCreateRequestDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .status(post.getStatus())
                .build();
        model.addAttribute("post", dto);
        model.addAttribute("owner", me);
        model.addAttribute("categories", me.getCategories());
        model.addAttribute("action", String.format("/blog/manage/posts/%d", postId));
        model.addAttribute("method", "put");
        return "page/myblog/postEditor";
    }

    // 수정 api
    @HxRequest
    @PostMapping("/blog/manage/posts/{postId}")
    public HtmxResponse updatePost(
            @PathVariable("postId") Long postId,
            @LoginUser User currentUser,
            @Valid @ModelAttribute("post") PostUpdateRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder().view("page/myblog/postEditor").preventHistoryUpdate().build();
        }
        postUseCase.updatePost(postId, requestDto, currentUser);
        return HtmxResponse.builder()
                .redirect("/blog/manage/posts")
                .build();
    }
}
