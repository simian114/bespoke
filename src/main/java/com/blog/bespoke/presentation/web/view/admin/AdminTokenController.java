package com.blog.bespoke.presentation.web.view.admin;

import com.blog.bespoke.application.usecase.token.TokenUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.infrastructure.web.htmx.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/token")
public class AdminTokenController {
    private final UserUseCase userUseCase;
    private final TokenUseCase tokenUseCase;

    @DeleteMapping("/{tokenId}")
    public HtmxResponse deleteTokens(@PathVariable("tokenId") Long tokenId) {
        tokenUseCase.deleteToken(tokenId);
        return HtmxResponse.builder()
                .trigger(
                        Toast.TRIGGER,
                        Toast.success("delete!")
                )
                .build();
    }

}
