package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.application.dto.response.S3PostImageResponseDto;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.S3PostImage;
import com.fasterxml.jackson.databind.cfg.ConfigOverride;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class PostUpdateRequestDto {
    @Size(min = 5, max = 100)
    @NotEmpty
    private String title;

    private String description;
    private String content;
    private Long categoryId;

    private MultipartFile cover;


    private Post.Status status;

    private S3PostImageResponseDto prevCover;
}
