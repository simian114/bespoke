package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.domain.model.post.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Data
public class PostUpdateRequestDto {
    @NotBlank
    @Size(min = 5, max = 50)
    private String title;

    @NotBlank
    @Size(min = 10, max = 150)
    private String description;
    private String content;
    private Long categoryId;

    private MultipartFile cover;

    private Post.Status status;

    private Long prevCoverId;
    private String prevCoverUrl;
    private String prevCoverOriginalFilename;

    // NOTE: category
    private List<UserResponseDto.CategoryResponseDto> categories;

}
