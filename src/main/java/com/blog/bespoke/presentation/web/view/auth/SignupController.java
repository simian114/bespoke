package com.blog.bespoke.presentation.web.view.auth;

import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SignupController {
    private final UserUseCase userUseCase;

    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("user") UserSignupRequestDto requestDto,
                             Model model,
                             @LoginUser User currentUser,
                             RedirectAttributes redirectAttributes
    ) {
        // 회원가입 성공하면 ...
        UserSignupRequestDto dto = UserSignupRequestDto.builder().build();
        model.addAttribute("user", dto);

        return "page/signup/signup";
    }

    @GetMapping("/signup/success")
    public HtmxResponse success() {
        return HtmxResponse.builder()
                .view("page/signup/success")
                .build();
    }

    // 성공하면, signup/success 로 이동
    @HxRequest
    @PostMapping("/signup")
    public HtmxResponse signup(@Valid @ModelAttribute("user") UserSignupRequestDto requestDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder()
                    .view("page/signup/signup :: form")
                    .trigger("reload:tinymce")
                    .preventHistoryUpdate()
                    .build();
        }
        try {
            userUseCase.signup(requestDto);
        } catch (BusinessException e) {
            /*
             * form 전체에 적용되는 에러는 ObjectError 를 사용하고
             * 필드 하나에 적용되는 에러는 FieldError 를 사용한다.
             */
            if (e.getMessage().equals(ErrorCode.OVER_AVATAR_LIMIT_SIZE.getMessage()) ||
                    e.getMessage().equals(ErrorCode.UNSUPPORTED_IMAGE.getMessage())
            ) {
                bindingResult.addError(new FieldError("user", "avatar", e.getMessage()));
            } else if (e.getMessage().equals(ErrorCode.EXISTS_EMAIL.getMessage())) {
                bindingResult.addError(new FieldError("user", "email", e.getMessage()));
            } else if (e.getMessage().equals(ErrorCode.EXISTS_NICKNAME.getMessage())) {
                bindingResult.addError(new FieldError("user", "nickname", e.getMessage()));
            } else {
                bindingResult.addError(new ObjectError("user", e.getMessage()));
            }

            return HtmxResponse.builder()
                    .view("page/signup/signup :: form")
                    .preventHistoryUpdate()
                    .trigger("reload:tinymce")
                    .build();
        } catch (Exception e) {
            return HtmxResponse.builder()
                    .view("page/signup/signup :: form")
                    .trigger("reload:tinymce")
                    .preventHistoryUpdate()
                    .build();
        }

        return HtmxResponse.builder()
                .redirect("/signup/success")
                .build();

    }

    // NOTE: 이메일 인증
    @GetMapping("/email-validation")
    public String emailValidation(@RequestParam(name = "code") String code) {
        userUseCase.emailValidation(code);
        return "page/signup/emailValidation";
    }

}

