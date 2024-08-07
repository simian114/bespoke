package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.request.CategoryCreateRequestDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.user.UserCategoryUseCase;
import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.user.CategoryUpdateCmd;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.Envelope;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BlogCategoryManageController {
    private final UserCategoryUseCase userCategoryUseCase;

    /**
     * 카테고리 생성 페이지
     */
    @GetMapping("/blog/manage/categories/new")
    public String categoryCreatePage(@ModelAttribute("category") CategoryCreateRequestDto requestDto,
                                     @LoginUser User currentUser,
                                     Model model) {
        model.addAttribute("owner", currentUser);
        model.addAttribute("action", "/blog/manage/categories");
        return "page/myblog/categoryForm";
    }

    /**
     * 카테고리 수정 페이지
     */
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

    /**
     * 카테고리 수정 api
     */
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

    /**
     * 카테고리 생성 api
     */
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

    /**
     * 카테고리 삭제 api
     */
    @Envelope("category delete success!")
    @ResponseBody
    @DeleteMapping("/blog/manage/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") Long categoryId,
                                 @LoginUser User currentUser
    ) {
        userCategoryUseCase.deleteCategory(categoryId, currentUser);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 visible 속성 변경
     */

    @PatchMapping("/blog/manage/categories/{categoryId}/{visible}")
    public HtmxResponse changeVisibility(@PathVariable("categoryId") Long categoryId,
                                         @PathVariable("visible") boolean visible,
                                         @LoginUser User currentUser,
                                         Model model) {
        UserResponseDto.CategoryResponseDto categoryResponseDto = userCategoryUseCase.changeVisibilty(categoryId, currentUser, visible);
        model.addAttribute("category", categoryResponseDto);
        return HtmxResponse.builder()
                .view("page/myblog/categoryTable :: category-item-row")
                .build();
    }

}
