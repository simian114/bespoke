package com.blog.bespoke.presentation.web.view;

import com.blog.bespoke.application.dto.request.postSearch.PostSearchForMainHomeRequestDto;
import com.blog.bespoke.application.dto.response.BannerFormResponseDto;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.PostSearchResponseDto;
import com.blog.bespoke.application.dto.response.search.CommonSearchResponseDto;
import com.blog.bespoke.application.usecase.banner.BannerFormSearchUseCase;
import com.blog.bespoke.application.usecase.post.PostSearchUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostSearchUseCase postSearchUseCase;
    private final BannerFormSearchUseCase bannerFormSearchUseCase;

    /**
     * NOTE
     * 홈페이지는 스태틱하게 가져가도록. 현재 헤더에서 security 의 isAuthenticated를 사용중인데
     * 그 부분도 동적으로 데이터 가져오게 변경
     * 이후에 홈페이지는 캐싱 후 사용자에게 제공
     * 1. http 캐싱
     * 2. 레디스 캐싱
     */
    @GetMapping({"", "/",})
    public HtmxResponse home(@ModelAttribute PostSearchForMainHomeRequestDto requestDto,
                             @LoginUser User currentUser,
                             @CookieValue(value = "hidePopupBanner", required = false) Boolean hidePopupBanner,
                             HtmxRequest htmxRequest,
                             Model model) {
        model.addAttribute("me", currentUser);

        PostSearchResponseDto posts = postSearchUseCase.postSearch(requestDto, currentUser);
        List<PostResponseDto> contents = posts.getContent();
        long totalElements = posts.getTotalElements();

        model.addAttribute("totalElements", totalElements);
        model.addAttribute("hasNextPage", posts.hasNext());
        model.addAttribute("posts", contents);
        model.addAttribute("page", posts.getPage());
        model.addAttribute("me", currentUser);

        /*
         * 무한스크롤 요청인 경우.
         * - isHtmxRequest(): htmx 요청인지 확인한다.
         * - isBoosted(): 페이지 이동인지 확인한다.
         */
        if (htmxRequest.isHtmxRequest() && !htmxRequest.isBoosted()) {
            return HtmxResponse.builder()
                    .view("page/home/home :: .post-list")
                    .build();
        }

        // TODO: Banner 는 반드시 캐싱할것.
        /*
         * heroBanners: 메인홈 페이지에 슬라이더를 만든다.
         *   - 메인홈 페이지 슬라이더
         * popupBanners: 메인홈 페이지 접속 시 나오는 팝업.
         *   - 메인홈 페이지 팝업
         *   - 쿠키를 확인하고, 쿠키가 있으면 팝업을 띄우지 않는다.
         *
         *
         * TODO: banner 는 반드시 캐싱이 필요하다. 캐싱 기간은 하루다.
         */
        List<BannerResponseDto> heroBanners = bannerFormSearchUseCase.getMainHeroBanner();
        model.addAttribute("heroBanners", heroBanners);

        if (hidePopupBanner == null || !hidePopupBanner) {
            List<BannerResponseDto> popupBanners = bannerFormSearchUseCase.getMainPopupBanner();
            model.addAttribute("popupBanners", popupBanners);
        }

        // NOTE: post list
        return HtmxResponse.builder()
                .view("page/home/home")
                .build();
    }

}
