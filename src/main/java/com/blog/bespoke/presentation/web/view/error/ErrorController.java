package com.blog.bespoke.presentation.web.view.error;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ErrorController {
    @ModelAttribute
    void initMode(@LoginUser User currentUser,
                  Model model) {
        model.addAttribute("me", currentUser);
    }

    @GetMapping({ "/errors"  })
    public String showErrorPage(Model model, @LoginUser User currentUser) {
        model.addAttribute("me", currentUser);
        return "error/error";
    }

}
