package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.request.BannerCreateRequestDto;
import com.blog.bespoke.application.dto.request.BannerFormCreateRequestDto;
import com.blog.bespoke.application.dto.request.EstimatedPaymentRequestDto;
import com.blog.bespoke.application.dto.request.search.banner.BannerFormSearchForManage;
import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.application.usecase.banner.BannerFormSearchUseCase;
import com.blog.bespoke.application.usecase.banner.BannerFormUseCase;
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
    private final BannerFormSearchUseCase bannerFormSearchUseCase;
    private final BannerFormUseCase bannerFormUseCase;

    @ModelAttribute
    public void handleCommonAttribute(@PathVariable(value = "bannerId", required = false) Long bannerId,
                                      @LoginUser User currentUser,
                                      Model model) {
        model.addAttribute("me", currentUser);
        model.addAttribute("nickname", currentUser.getNickname());
        if (bannerId != null) {
            model.addAttribute("bannerId", bannerId);
        }
    }

    @GetMapping("/new")
    public HtmxResponse createBannerFormPage(@LoginUser User currentUser,
                                             Model model
    ) {
        BannerCreateRequestDto dto = BannerCreateRequestDto.builder()
                .build();
        model.addAttribute("banner", dto);
        model.addAttribute("uiTypes", BannerUiType.values());
        return HtmxResponse.builder()
                .view("page/myblog/banner/bannerForm")
                .build();
    }


    @PostMapping("/new")
    public HtmxResponse createBanner(@ModelAttribute("banner") BannerCreateRequestDto dto,
                                     BindingResult bindingResult,
                                     @LoginUser User currentUser,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder()
                    .view("page/myblog/banner/bannerForm")
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

    @GetMapping("/{bannerId}/form")
    public HtmxResponse bannerForm(@PathVariable("bannerId") Long bannerId,
                                   @LoginUser User currentUser,
                                   Model model) {
        // TODO: banner
        BannerResponseDto banner = bannerUseCase.getBannerById(bannerId);

        BannerFormCreateRequestDto bannerFormCreateRequestDto = new BannerFormCreateRequestDto();
        model.addAttribute("banner", banner);
        model.addAttribute("bannerForm", bannerFormCreateRequestDto);

        EstimatedPaymentRequestDto estimatedPaymentRequestDto = new EstimatedPaymentRequestDto();
        estimatedPaymentRequestDto.setDuration(2);
        estimatedPaymentRequestDto.setUiType(banner.getUiType());
        Long estimatedPayment = bannerFormUseCase.calculatedEstimatedPayment(estimatedPaymentRequestDto, currentUser);

        // TODO: estimated payment 계산
        model.addAttribute("estimatedPayment", estimatedPayment);
        return HtmxResponse.builder()
                .view("page/myblog/banner/bannerForm/bannerFormForm")
                .build();
    }

    @PostMapping("/{bannerId}/form")
    public HtmxResponse bannerFormCreate(@ModelAttribute("bannerForm") BannerFormCreateRequestDto requestDto,
                                         BindingResult bindingResult,
                                         @PathVariable("bannerId") Long bannerId,
                                         @LoginUser User currentUser) {
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder()
                    .view("page/myblog/banner/bannerFormForm")
                    .preventHistoryUpdate()
                    .build();
        }
        bannerFormUseCase.createBannerForm(requestDto, bannerId, currentUser);

        return HtmxResponse.builder()
                .redirect(String.format("/blog/manage/banners/%d/request", bannerId))
                .build();
    }

    @GetMapping("/{bannerId}/form/estimated-payment")
    public HtmxResponse getEstimatedPayment(@ModelAttribute("estimatedPayment") EstimatedPaymentRequestDto requestDto,
                                            @LoginUser User currentUser,
                                            Model model) {

        Long estimatedPayment = bannerFormUseCase.calculatedEstimatedPayment(requestDto, currentUser);
        model.addAttribute("estimatedPayment", estimatedPayment);

        return HtmxResponse.builder()
                .view("page/myblog/banner/bannerForm/bannerFormForm :: estimated-payment")
                .build();
    }

    @GetMapping("/{bannerId}/request")
    public HtmxResponse requestPublish(@ModelAttribute BannerFormSearchForManage requestDto,
                                       @PathVariable("bannerId") Long bannerId,
                                       @LoginUser User currentUser,
                                       Model model) {
        requestDto.setBannerId(bannerId);

        CommonSearchResponseDto<BannerFormResponseDto> res = bannerFormSearchUseCase.searchBannerForm(requestDto, currentUser);

        model.addAttribute("cond", requestDto);

        model.addAttribute("items", res.getContent());
        model.addAttribute("totalElements", res.getTotalElements());
        model.addAttribute("isLast", !res.hasNext());
        model.addAttribute("page", res.getPage());


        /*
         * 가져온 form list 중 상태를 확인하고 model 에 추가한다.
         * 1. 현재 진행중인
         * 2. (현재 진행중인게 있으면) 생성 불가능으로 세팅
         */
        BannerFormResponseDto currentForm = bannerFormUseCase.getOnGoingBannerForm(res.getContent());
        model.addAttribute("currentForm", currentForm);
        model.addAttribute("canCreateNewForm", currentForm == null);

        return HtmxResponse.builder()
                .view("page/myblog/banner/bannerRequestPublish")
                .build();
    }


}
