package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.request.BannerCreateRequestDto;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.application.usecase.banner.BannerUseCase;
import com.blog.bespoke.domain.model.banner.BannerUiType;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import com.blog.bespoke.infrastructure.web.htmx.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blog/manage/banners")
@RequiredArgsConstructor
public class BlogBannerManageController {
    private final BannerUseCase bannerUseCase;

    @ModelAttribute
    public void handleCommonAttribute(@LoginUser User currentUser,
                                      Model model) {
        model.addAttribute("me", currentUser);
        model.addAttribute("nickname", currentUser.getNickname());
//        if (bannerId != null) {
//            model.addAttribute("bannerId", bannerId);
//        }
    }

    @GetMapping("/new")
    public HtmxResponse createBannerForm(@LoginUser User currentUser,
                                         Model model
    ) {
        BannerCreateRequestDto dto = BannerCreateRequestDto.builder()
                .build();
        model.addAttribute("banner", dto);
        model.addAttribute("uiTypes", BannerUiType.values());
        return HtmxResponse.builder()
                .view("page/myblog/bannerForm")
                .build();
    }

    @PostMapping("/new")
    public HtmxResponse createBanner(@ModelAttribute("banner") BannerCreateRequestDto dto,
                                     BindingResult bindingResult,
                                     @LoginUser User currentUser,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder()
                    .view("page/myblog/bannerForm")
                    .preventHistoryUpdate()
                    .build();
        }
        BannerResponseDto banner = bannerUseCase.createBanner(dto, currentUser);

        return HtmxResponse.builder()
                .redirect("/blog/manage/banners")
                .build();
    }

    @DeleteMapping("/{bannerId}")
    public HtmxResponse deleteBanner(@PathVariable("bannerId") Long bannerId,
                                     @LoginUser User currentUser) {
        return HtmxResponse.builder()
                .trigger(Toast.TRIGGER,
                        Toast.info("success"))
                .build();
    }

}
