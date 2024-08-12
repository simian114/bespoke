package com.blog.bespoke.presentation.web.view.admin;

import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostStatusCmd;
import com.blog.bespoke.domain.model.post.PostUpdateCmd;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import com.blog.bespoke.infrastructure.web.htmx.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/post")
@RequiredArgsConstructor
public class AdminPostController {
    private final PostUseCase postUseCase;
    // post editor 는 그거 그냥 사용하기 대신, action url 을 model 에서 지정하게끔하자.

    @PatchMapping("/{postId}/status/{status}")
    public HtmxResponse changeStatus(@PathVariable("postId") Long postId,
                                     @PathVariable("status")Post.Status status,
                                     @LoginUser User currentUser,
                                     Model model) {
        PostStatusCmd cmd = new PostStatusCmd();
        cmd.setStatus(status);
        PostResponseDto dto = postUseCase.changeStatus(postId, cmd, currentUser);
        model.addAttribute("post", dto);

        return HtmxResponse.builder()
                .view("page/admin/post/post :: post-item-row")
                .build();
    }

    @DeleteMapping("/{postId}")
    public HtmxResponse deletePost(@PathVariable("postId") Long postId,
                                   @LoginUser User currentUser) {
        postUseCase.deletePost(postId, currentUser);

        return HtmxResponse.builder()
                .trigger(Toast.TRIGGER,
                        Toast.info("delete success!"))
                .build();
    }

}



