package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.application.dto.response.S3PostImageResponseDto;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.S3PostImage;
import com.fasterxml.jackson.databind.cfg.ConfigOverride;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class PostUpdateRequestDto {
    @NotBlank
    @Size(min = 5, max = 50)
    private String title;

    @NotBlank
    @Size(min = 10, max = 100)
    private String description;
    private String content;
    private Long categoryId;

    private MultipartFile cover;

    private Post.Status status;

    private Long prevCoverId;
    private String prevCoverUrl;
    private String prevCoverOriginalFilename;
}
