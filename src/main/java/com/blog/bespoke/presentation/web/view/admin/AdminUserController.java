package com.blog.bespoke.presentation.web.view.admin;

import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import com.blog.bespoke.infrastructure.web.htmx.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserUseCase userUseCase;

    @PatchMapping("/{userId}/activate")
    public HtmxResponse changeStatus(@PathVariable("userId") Long userId,
                                     @LoginUser User currentUser,
                                     Model model) {
        UserResponseDto dto = userUseCase.userActivate(userId, currentUser);
        model.addAttribute("user", dto);
        return HtmxResponse.builder()
                .view("page/admin/user/fragments :: user-item-row")
                .trigger(Toast.TRIGGER,
                        Toast.info("success!"))
                .build();
    }

    @PatchMapping("/{userId}/ban")
    public HtmxResponse banUser(@PathVariable("userId") Long userId,
                                @LoginUser User currentUser,
                                HtmxRequest htmxRequest,
                                Model model) {
        String promptResponse = htmxRequest.getPromptResponse();

        int days = promptResponse == null || promptResponse.isBlank()
                ? 3
                : Integer.parseInt(promptResponse);

        UserResponseDto dto = userUseCase.bannedUntil(userId, days, currentUser);
        model.addAttribute("user", dto);

        return HtmxResponse.builder()
                .view("page/admin/user/fragments :: user-item-row")
                .trigger(Toast.TRIGGER,
                        Toast.info("success!"))
                .build();
    }

    @PatchMapping("/{userId}/unban")
    public HtmxResponse unbanUser(@PathVariable("userId") Long userId,
                                  @LoginUser User currentUse,
                                  Model model) {
        UserResponseDto dto = userUseCase.unban((userId), currentUse);
        model.addAttribute("user", dto);
        return HtmxResponse.builder()
                .view("page/admin/user/fragments :: user-item-row")
                .trigger(Toast.TRIGGER,
                        Toast.info("success!"))
                .build();
    }

    @DeleteMapping("/{userId}")
    public HtmxResponse deleteUser(@PathVariable("userId") Long userId,
                                   @LoginUser User currentUser,
                                   Model model) {

        userUseCase.deleteUser(userId, currentUser);
        return HtmxResponse.builder().build();
    }

    @PostMapping("/{userId}/role/advertiser")
    public HtmxResponse addAdvertiserRole(@PathVariable("userId") Long userId) {
        UserResponseDto userResponseDto = userUseCase.addRole(userId, Role.Code.ADVERTISER);
        return HtmxResponse.builder()
                .trigger(
                        Toast.TRIGGER,
                        Toast.success("hello world")
                )
                .build();
    }


}
