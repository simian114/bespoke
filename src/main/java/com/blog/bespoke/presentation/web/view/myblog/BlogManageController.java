package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.dto.request.UserUpdateRequestDto;
import com.blog.bespoke.application.dto.request.postSearch.PostSearchForManageRequestDto;
import com.blog.bespoke.application.dto.request.search.banner.BannerSearchForManage;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.PostSearchResponseDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.application.usecase.banner.BannerSearchUseCase;
import com.blog.bespoke.application.usecase.post.PostSearchUseCase;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserCategoryUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class BlogManageController {
    private final UserUseCase userUseCase;
    private final PostUseCase postUseCase;
    private final PostSearchUseCase postSearchUseCase;
    private final UserCategoryUseCase userCategoryUseCase;
    private final BannerSearchUseCase bannerSearchUseCase;

    @ModelAttribute
    public String handleCommonAttribute(@LoginUser User currentUser,
                                        Model model) {
        model.addAttribute("me", currentUser);
        model.addAttribute("nickname", currentUser.getNickname());
        model.addAttribute("isAdvertiser", currentUser.isAdvertiser());
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

    /**
     * 배너 리스트
     */

//    @ModelAttribute PostSearchForManageRequestDto requestDto,
    @GetMapping("/blog/manage/banners")
    public HtmxResponse bannerManage(@LoginUser User currentUser,
                                     @ModelAttribute BannerSearchForManage requestDto,
                                     Model model) {

        requestDto.setNickname(currentUser.getNickname());
        CommonSearchResponseDto<BannerResponseDto> res = bannerSearchUseCase.searchBanner(requestDto, currentUser);

        model.addAttribute("cond", requestDto);

        model.addAttribute("items", res.getContent());
        model.addAttribute("totalElements", res.getTotalElements());
        model.addAttribute("isLast", !res.hasNext());
        model.addAttribute("isFirst", !res.hasPrevious());
        model.addAttribute("totalPages", res.getTotalPage());
        model.addAttribute("page", res.getPage());

        return HtmxResponse.builder()
                .view("page/myblog/banner/bannerTable")
                .build();
    }
}
