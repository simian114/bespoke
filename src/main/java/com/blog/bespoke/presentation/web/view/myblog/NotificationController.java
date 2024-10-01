package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.response.NotificationResponseDto;
import com.blog.bespoke.application.usecase.notification.NotificationUseCase;
import com.blog.bespoke.domain.model.notification.NotificationSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationUseCase notificationUseCase;

    @GetMapping("/notification")
    public String notificationListPage(@LoginUser User currentUser,
                                       Model model) {
        model.addAttribute("me", currentUser);
        model.addAttribute("page", 0);
        return "page/notification/notification";
    }

    @HxRequest
    @GetMapping("/notification/list")
    public String notificationList(@ModelAttribute NotificationSearchCond cond,
                                   @LoginUser User currentUser,
                                   Model model) {
        cond.setRecipientId(currentUser.getId());

        Page<NotificationResponseDto> res = notificationUseCase.search(cond, currentUser);
        model.addAttribute("notifications", res.getContent());
        model.addAttribute("totalElements", res.getTotalElements());
        model.addAttribute("hasNextPage", res.hasNext());
        model.addAttribute("hasPreviousPage", res.hasPrevious());
        model.addAttribute("page", res.getPageable().getPageNumber() + 1);
        return "page/notification/notification :: notification-list";
    }

    @PatchMapping("/notification/{notificationId}")
    @ResponseBody
    public ResponseEntity<?> checkReadNotification(@PathVariable("notificationId") Long notificationId) {
        notificationUseCase.readNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

}
