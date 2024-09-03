package com.blog.bespoke.presentation.web.view.banner;

import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.application.usecase.banner.BannerFormSearchUseCase;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class BannerController {
    private final BannerFormSearchUseCase bannerFormSearchUseCase;

    /**
     * 굉장히 자주 호출되는 api 이므로 캐싱을 반드시 한다.
     * 클라이언트 캐싱은 어떨까?
     */
    @GetMapping("/banner/top-banner")
    public HtmxResponse topBanner(Model model) {
        List<BannerResponseDto> banners = bannerFormSearchUseCase.getTopBanners();
        if (banners.isEmpty()) {
            return HtmxResponse.builder()
                    .build();
        }
        Random random = new Random();
        int i = random.nextInt(banners.size());

        BannerResponseDto banner = banners.get(i);

        model.addAttribute("banner", banner);
        model.addAttribute("banners", banners);
        return HtmxResponse.builder()
                .view("fragments/banner/topBanner :: top-banner")
                .build();
    }
}
