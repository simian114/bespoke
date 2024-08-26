package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import com.blog.bespoke.infrastructure.web.htmx.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class BlogRoleManageController {
    private final UserUseCase userUseCase;

    @PostMapping({"/blog/manage/role/advertiser"})
    public HtmxResponse myBlog(@LoginUser User currentUser) {
        // TODO: token 생성
        userUseCase.requestAdvertiserRole(currentUser.getId());

        return HtmxResponse.builder()
                .trigger(
                        Toast.TRIGGER,
                        Toast.success("Request Success!")
                )
                .redirect("/blog/manage/posts")
                .build();
    }

}
