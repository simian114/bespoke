package com.blog.bespoke.presentation.web.view.auth;

import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SignupPageController {
    private final UserUseCase userUseCase;

    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("user") UserSignupRequestDto requestDto,
                             Model model,
                             @LoginUser User currentUser,
                             RedirectAttributes redirectAttributes
    ) {
        if (currentUser != null) {
            redirectAttributes.addFlashAttribute("flash", "접근하지 못하는 경로입니다.");
            return "redirect:/";
        }
        // 회원가입 성공하면 ...
        UserSignupRequestDto dto = UserSignupRequestDto.builder().build();
        model.addAttribute("user", dto);

        return "page/signup/signup";
    }

    // 성공하면, signup/success 로 이동
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") UserSignupRequestDto requestDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            bindingResult.addError(new FieldError("user", "email", "이메일 중복됨"));
            return "/page/signup/signup";
        }
        try {
            userUseCase.signup(requestDto);
        } catch (BusinessException e) {
            // NOTE: global error - 이메일 중복됨 / nickname 중복됨
            bindingResult.addError(new ObjectError("user", e.getMessage()));
            return "/page/signup/signup";
        }

        redirectAttributes.addFlashAttribute("signupSuccess", true);
        return "redirect:/signup/success";
    }

    @GetMapping("/signup/success")
    public String signupSuccess(Model model) {
        boolean fromSignup = Boolean.TRUE.equals(model.asMap().get("signupSuccess"));
        if (!fromSignup) {
            return "redirect:/";
        }
        return "/page/signup/success";
    }
}

