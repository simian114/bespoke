package com.blog.bespoke.presentation.web.view.admin;

import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.application.usecase.banner.BannerFormSearchUseCase;
import com.blog.bespoke.application.usecase.banner.BannerFormUseCase;
import com.blog.bespoke.domain.model.banner.BannerFormStatus;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/banner/{bannerId}")
@RequiredArgsConstructor
public class AdminBannerController {
    private final BannerFormUseCase bannerFormUseCase;
    private final BannerFormSearchUseCase bannerFormSearchUseCase;


    @ModelAttribute
    void init(@PathVariable("bannerId") Long bannerId,
              @LoginUser User currentUser,
              Model model) {
        model.addAttribute("bannerId", bannerId);
    }


    @GetMapping
    public HtmxResponse bannerFormAuditPage(@PathVariable("bannerId") Long bannerId,
                                        @LoginUser User currentUser,
                                        Model model) {
        BannerFormResponseDto bannerForm = bannerFormUseCase.getBannerFormById(bannerId);

        model.addAttribute("bannerForm", bannerForm);
        model.addAttribute("banner", bannerForm.getBannerSnapshot());

        return HtmxResponse.builder()
                .view("page/admin/banner/bannerFormAudit")
                .build();
    }

    @PatchMapping("/audit/{ok}")
    public HtmxResponse bannerFormAudit(@PathVariable("bannerId") Long bannerId,
                                        @PathVariable("ok") boolean ok,
                                        HtmxRequest htmxRequest,
                                        Model model) {
        // TODO: result
        String reason = ok ? null : htmxRequest.getPromptResponse();
        BannerFormResponseDto dto = bannerFormUseCase.audit(bannerId, ok, reason);

        return HtmxResponse.builder()
                .redirect("/admin/banner")
                .build();
    }
}
