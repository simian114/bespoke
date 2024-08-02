package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.request.CategoryCreateRequestDto;
import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.dto.request.UserUpdateRequestDto;
import com.blog.bespoke.application.dto.request.postSearch.PostSearchForManageRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.PostSearchResponseDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.application.usecase.post.PostSearchUseCase;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserCategoryUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.post.PostSearchCond;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class BlogManageController {
    private final UserUseCase userUseCase;
    private final PostUseCase postUseCase;
    private final PostSearchUseCase postSearchUseCase;
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
        UserResponseDto me = userUseCase.getUserForProfileEdit(currentUser.getId());
        UserUpdateRequestDto user = UserUpdateRequestDto.builder()
                .name(me.getName())
                .introduce(me.getUserProfile().getIntroduce())
                .prevAvatarId(me.getAvatar() != null ? me.getAvatar().getId() : null)
                .prevAvatarUrl(me.getAvatarUrl())
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
                                      @LoginUser User currentUser) {
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder()
                    .preventHistoryUpdate()
                    .view("page/myblog/profile")
                    .build();
        }

        try {
            userUseCase.updateUser(requestDto, currentUser.getId());
        } catch (BusinessException e) {
            if (e.getStatusCode() == ErrorCode.UNSUPPORTED_IMAGE.getStatusCode() ||
                    e.getStatusCode() == ErrorCode.OVER_AVATAR_LIMIT_SIZE.getStatusCode()
            ) {
                bindingResult.addError(new FieldError("user", "avatar", e.getMessage()));
            } else {
                bindingResult.addError(new ObjectError("user", e.getMessage()));
            }
            return HtmxResponse.builder()
                    .preventHistoryUpdate()
                    .view("page/myblog/profile")
                    .build();
        } catch (Exception e) {
            return HtmxResponse.builder()
                    .preventHistoryUpdate()
                    .view("page/myblog/profile")
                    .build();
        }

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
            @ModelAttribute PostSearchForManageRequestDto requestDto,
            @LoginUser User currentUser,
            Model model) {
        // TODO: 정리 필요함
        requestDto.setNickname(currentUser.getNickname());
        model.addAttribute("posts", null);
        PostSearchResponseDto posts = postSearchUseCase.postSearch(requestDto, currentUser);

        model.addAttribute("posts", posts.getContent());

        // 페이지네이션 관련된 객체로 하나 만들고 그거 넣으면 페이제네이션 완성되도록 하기.
        model.addAttribute("totalElements", posts.getTotalElements());
        model.addAttribute("isLast", posts.hasNext());
        model.addAttribute("isFirst", posts.hasPrevious());
        model.addAttribute("totalPages", posts.getTotalPage());
        model.addAttribute("page", posts.getPage());

        return "page/myblog/postTable";
    }

    /**
     * 게시글 생성 임시 페이지.
     * 해당 페이지에서는 게시글 새성
     */
    @GetMapping("/blog/manage/posts/new")
    public String createPostPage(@LoginUser User currentUser) {
        PostCreateRequestDto dto = PostCreateRequestDto.builder()
                .title("")
                .build();
        PostResponseDto post = postUseCase.writePostAsTempSave(dto, currentUser);
        return String.format("redirect:/blog/manage/posts/%d/edit", post.getId());
    }


    /**
     * 카테고리 리스트
     */
    @GetMapping("/blog/manage/categories")
    public String categoryManage(@LoginUser User currentUser,
                                 Model model) {
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
                                   Model model) {
        UserResponseDto userWithCategory = userCategoryUseCase.getUserWithCategory(currentUser.getId());
        UserResponseDto.CategoryResponseDto category = userWithCategory.getCategories().stream().filter(c -> c.getId().equals(categoryId))
                .findAny().orElseThrow();

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
        if (bindingResult.hasErrors()) {
            return "page/myblog/categoryForm";
        }
        userCategoryUseCase.updateCategory(categoryId, requestDto, currentUser.getId());
        return "redirect:/blog/manage/categories";
    }


    @PostMapping("/blog/manage/categories")
    public String categoryCreate(@LoginUser User currentUser,
                                 @Valid @ModelAttribute("category") CategoryCreateRequestDto requestDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "page/myblog/categoryForm";
        }
        userCategoryUseCase.createCategory(requestDto, currentUser.getId());
        return "redirect:/blog/manage/categories";
    }

    @ResponseBody
    @DeleteMapping("/blog/manage/categories/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId,
                                 @LoginUser User currentUser
    ) {
        userCategoryUseCase.deleteCategory(categoryId, currentUser);
        return "";
    }

}