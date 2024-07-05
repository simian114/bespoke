package com.blog.bespoke.presentation.web.view.auth;

import com.blog.bespoke.application.dto.request.LoginRequestDto;
import com.blog.bespoke.application.dto.response.LoginResponseDto;
import com.blog.bespoke.application.usecase.AuthUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginPageController {
    private final AuthUseCase authUseCase;

    @GetMapping("/login")
    public String loginPage(Model model, @LoginUser User currentUser, RedirectAttributes redirectAttributes) {
        if (currentUser != null) {
            redirectAttributes.addFlashAttribute("flash", "접근하지 못하는 경로입니다.");
            return "redirect:/";
        }

        LoginRequestDto dto = new LoginRequestDto();
        model.addAttribute("user", dto);

        return "page/login/login";
    }

    // NOTE: htmx 을 이용하면

    /**
     * htmx 을 이용하면 redirect 가 정상동작 하지 않는다.
     * 1. url 이 그대로 login 페이지다.
     * 2. 상위 view 를 그대로 남겨놓고, redirect 의 페이지가 그 내부에 들어간다 -> header 가 두개다
     * login 같은 경우에는 htmx 을 사용하면 안되나? 아니면 다른 방법이 있는걸까?
     * htmx 의 헤더를 수정하면 될거같기도하다.
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") LoginRequestDto requestDto,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model
    ) {
        // NOTE: bean validation
        if (bindingResult.hasErrors()) {
            // field 에러로 넣기
            model.addAttribute("loginError", "email 과 password 를 확인해주세요");
            // NOTE: 이렇게 하면 login 페이지에서 form 만 전송함
            return "page/login/login";
        }
        try {
            LoginResponseDto token = authUseCase.login(requestDto);
            response.addCookie(new Cookie("access_token", token.getAccessToken()));
            response.addCookie(new Cookie("refresh_token", token.getAccessToken()));

            // 성공 시 어떻게 할지..
            // htmx의 redirect 를 하면 뭔가 이상함 url도 변경되지 않고 기존 레이아웃 안으로 들어가버림
            return "redirect:/";
        } catch (Exception e) {
            bindingResult.addError(new ObjectError("user", "email 과 password 확인 요망"));
            // NOTE: htmx 를 이용하게 되면 이 동작으로 변경해야한다.
            // return "page/login/login :: form";
            return "page/login/login";
        }
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletResponse response) {
        log.info("hello world");
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return "redirect:/";
    }

}