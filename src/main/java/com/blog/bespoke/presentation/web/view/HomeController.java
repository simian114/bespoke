package com.blog.bespoke.presentation.web.view;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"", "/", "/home"})
    public String home(@LoginUser User currentUser, Model model) {
        // currentUser
        return "page/home/home";
    }
    // 공통으로 model 에 user 는 넣기
}
