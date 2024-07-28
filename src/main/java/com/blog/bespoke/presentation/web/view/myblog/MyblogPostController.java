package com.blog.bespoke.presentation.web.view.myblog;

import com.blog.bespoke.application.dto.request.PostUpdateRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.S3PostImageResponseDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostStatusCmd;
import com.blog.bespoke.domain.model.post.S3PostImage;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.Envelope;
import com.blog.bespoke.infrastructure.web.argumentResolver.annotation.LoginUser;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class MyblogPostController {
    private final PostUseCase postUseCase;
    private final UserUseCase userUseCase;

    @ModelAttribute
    public String handleCommonAttribute(@PathVariable("postId") Long postId,
                                        @LoginUser User currentUser,
                                        Model model) {
        model.addAttribute("me", currentUser);
        model.addAttribute("nickname", currentUser.getNickname());
        if (postId != null) {
            model.addAttribute("postId", postId);
        }
        return "";
    }

    /**
     * 게시글 수정페이지
     */
    @GetMapping("/blog/manage/posts/{postId}/edit")
    public String editPostPage(@PathVariable("postId") Long postId,
                               @LoginUser User currentUser,
                               Model model) {
        UserResponseDto me = userUseCase.getUserForPostWrite(currentUser.getId());
        PostResponseDto post = postUseCase.getPostForEdit(postId);

        PostUpdateRequestDto dto = PostUpdateRequestDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .prevCoverId(post.getCover() != null ? post.getCover().getId() : null)
                .prevCoverUrl(post.getCover() != null ? post.getCover().getUrl() : null)
                .prevCoverOriginalFilename(post.getCover() != null ? post.getCover().getOriginalFilename() : null)
                .status(post.getStatus())
                .build();

        // category list 또한 list 형태로 제공해야할듯?
        model.addAttribute("post", dto);
        model.addAttribute("categories", me.getCategories());
        model.addAttribute("imageUploadUrl", String.format("/blog/manage/posts/%d/image", postId));
        return "page/myblog/postEditor";
    }

    /**
     * 게시글 수정 api
     * 커버 이미지를 s3 에 업로드 하고 객체로 생성하는 책임을 가진다.
     */
    @HxRequest
    @PostMapping("/blog/manage/posts/{postId}/edit")
    public HtmxResponse updatePost(
            @PathVariable("postId") Long postId,
            @LoginUser User currentUser,
            @Valid @ModelAttribute("post") PostUpdateRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // postUpdateRequestDto 를 다시 set?
            // category list..!
            return HtmxResponse.builder()
                    .view("page/myblog/postEditor :: form")
                    .preventHistoryUpdate()
                    .build();
        }
        postUseCase.updatePost(postId, requestDto, currentUser);
        return HtmxResponse.builder()
                .redirect("/blog/manage/posts")
                .build();
    }

    /**
     * 일반 이미지 업로드
     */
    @PostMapping("/blog/manage/posts/{postId}/image")
    @ResponseBody
    @Envelope("image upload success!")
    public ResponseEntity<?> uploadImageToPost(@PathVariable("postId") Long postId,
                                               @RequestParam("file") MultipartFile file,
                                               @LoginUser User currentUser) {
        S3PostImageResponseDto responseDto = postUseCase.uploadImage(file, S3PostImage.Type.CONTENT, postId, currentUser);
        return ResponseEntity.ok(responseDto);
    }


    /**
     * 삭제 실패한 경우 처리해야함.
     * 삭제 된 게시글은 리스트에 보이면 안됨 -> search 에서 삭제 된 게시글은 알아서 걸러져야 한다.
     */
    @HxRequest
    @DeleteMapping("/blog/manage/posts/{postId}")
    @ResponseBody
    public String deletePost(@PathVariable("postId") Long postId,
                             @LoginUser User currentUser) {
        try {
            postUseCase.deletePost(postId, currentUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    /**
     * 게시글 상태 변경
     */
    @HxRequest
    @PatchMapping("/blog/manage/posts/{postId}/status/{status}")
    public HtmxResponse changeStatus(@PathVariable("postId") Long postId,
                                     @PathVariable("status") Post.Status status,
                                     @LoginUser User currentUser,
                                     Model model) {

        // TODO: 하나 집어서 수정할 수 있게 변경
        PostStatusCmd cmd = new PostStatusCmd();
        cmd.setStatus(status);
        PostResponseDto postResponseDto = postUseCase.changeStatus(postId, cmd, currentUser);
        model.addAttribute("post", postResponseDto);

        return HtmxResponse.builder()
                .view("page/myblog/postTable :: post-item-row")
                .build();
    }

}
