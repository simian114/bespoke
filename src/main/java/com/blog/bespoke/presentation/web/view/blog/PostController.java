package com.blog.bespoke.presentation.web.view.blog;

import com.blog.bespoke.application.dto.request.CommentCreateRequestDto;
import com.blog.bespoke.application.dto.request.CommentUpdateRequestDto;
import com.blog.bespoke.application.dto.response.CommentResponseDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.post.PostCommentUseCase;
import com.blog.bespoke.application.usecase.post.PostLikeUseCase;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserFollowUseCase;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostUseCase postUseCase;
    private final PostCommentUseCase postCommentUseCase;
    private final PostLikeUseCase postLikeUseCase;
    private final UserFollowUseCase userFollowUseCase;

    @ModelAttribute
    public void handleCommonAttribute(Model model,
                                      @LoginUser User currentUser,
                                      @PathVariable(value = "postId", required = false) Long postId) {
        model.addAttribute("me", currentUser);
        model.addAttribute("postId", postId);
    }

    @GetMapping("/blog/posts/{postId}")
    public String postDetailPage(@PathVariable("postId") Long postId,
                                 @LoginUser User currentUser,
                                 Model model) {
        PostResponseDto post = postUseCase.showPostById(postId, currentUser);

        model.addAttribute("me", currentUser);
        model.addAttribute("isOwner", currentUser != null && currentUser.getNickname().equals(post.getAuthor().getNickname()));
        model.addAttribute("post", post);
        model.addAttribute("owner", post.getAuthor());
        boolean b = userFollowUseCase.checkFollow(currentUser, post.getAuthor().getId());
        model.addAttribute("follow", b);
        model.addAttribute("comment", CommentCreateRequestDto.builder().content("").build());
        model.addAttribute("postId", postId);

        // check already follow

        // TODO: 댓글...
        // TODO: floating TOC 만들기. 모바일 환경에서는 최상단에 details / summary 형태로 구현
        return "page/post/post";
    }

    @HxRequest
    @GetMapping("/blog/posts/{postId}/comments")
    public HtmxResponse getComments(@PathVariable("postId") Long postId,
                                    @LoginUser User currentUser,
                                    Model model) {
        List<CommentResponseDto> comments = postCommentUseCase.getCommentsByPostId(postId);
        model.addAttribute("comments", comments);
        return HtmxResponse.builder()
                .view("page/post/fragments :: comment-list")
                .build();
    }

    @HxRequest
    @PostMapping("/blog/posts/{postId}/comments")
    public HtmxResponse addComments(@PathVariable("postId") Long postId,
                                    @LoginUser User currentUser,
                                    Model model,
                                    @Valid @ModelAttribute("comment") CommentCreateRequestDto requestDto,
                                    BindingResult bindingResult) {
        // NOTE: 정상적인 경로로 요청을 했다면, 사실 여기로 올 수 없다.
        model.addAttribute("postId", postId);
        if (bindingResult.hasErrors()) {
            return HtmxResponse.builder()
                    .view("page/post/fragments :: comment-form")
                    .preventHistoryUpdate()
                    .build();
        }
        CommentResponseDto commentResponseDto = postCommentUseCase.addComment(requestDto, postId, currentUser);
        model.addAttribute("comment", commentResponseDto);
        model.addAttribute("postId", postId);
        return HtmxResponse.builder()
                .view("page/post/fragments :: comment-item")
                .preventHistoryUpdate()
                .build();
    }

    @HxRequest
    @DeleteMapping("/blog/posts/{postId}/comments/{commentId}")
    @ResponseBody
    public String deleteComment(@PathVariable("commentId") Long commentId,
                                @PathVariable("postId") Long postId,
                                @LoginUser User currentUser,
                                Model model) {
        postCommentUseCase.deleteComment(commentId, postId, currentUser);
        return "Comment deleted successfully";
    }

    @HxRequest
    @PostMapping("/blog/posts/{postId}/comments/{commentId}/edit")
    public HtmxResponse updateComment(@PathVariable("commentId") Long commentId,
                                      @ModelAttribute CommentUpdateRequestDto requestDto,
                                      @LoginUser User currentUser,
                                      Model model) {
        CommentResponseDto comment = postCommentUseCase.updateComment(commentId, requestDto, currentUser);
        model.addAttribute("comment", comment);
        return HtmxResponse.builder()
                .view("page/post/fragments :: comment-item")
                .build();
    }


    @HxRequest
    @PostMapping("/blog/posts/{postId}/like")
    public String postLike(@PathVariable("postId") Long postId,
                           @LoginUser User currentuser,
                           Model model) {

        PostResponseDto post = postLikeUseCase.likePost(postId, currentuser);

        model.addAttribute("post", post);
        return "page/post/post :: like";
    }

    @HxRequest
    @DeleteMapping("/blog/posts/{postId}/like")
    public HtmxResponse cancelPostLike(@PathVariable("postId") Long postId,
                                       @LoginUser User currentuser,
                                       Model model) {
        PostResponseDto post = postLikeUseCase.cancelLikePost(postId, currentuser);
        model.addAttribute("post", post);
        return HtmxResponse.builder()
                .view("page/post/post :: cancel-like")
                .build();
    }

    // currentUser
    @HxRequest
    @PostMapping("/blog/user/{userId}/follow")
    public HtmxResponse follow(@PathVariable("userId") Long userId,
                               @LoginUser User currentUser,
                               Model model) {
        UserResponseDto follow = userFollowUseCase.follow(userId, currentUser);
        model.addAttribute("follow", true);
        model.addAttribute("post", Post.builder().author(User.builder().id(userId).build()).build());
        return HtmxResponse.builder()
                .view("page/post/post :: unfollow")
                .build();
    }

    @HxRequest
    @DeleteMapping("/blog/user/{userId}/follow")
    public HtmxResponse unfollow(@PathVariable("userId") Long userId,
                                 @LoginUser User currentUser,
                                 Model model) {
        userFollowUseCase.unfollow(userId, currentUser);
        model.addAttribute("post", Post.builder().author(User.builder().id(userId).build()).build());
        model.addAttribute("follow", false);

        return HtmxResponse.builder()
                .view("page/post/post :: follow")
                .build();
    }

}

