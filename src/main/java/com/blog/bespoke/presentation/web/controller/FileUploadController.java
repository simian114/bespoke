package com.blog.bespoke.presentation.web.controller;

import com.blog.bespoke.domain.model.post.S3PostImage;
import com.blog.bespoke.infrastructure.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileUploadController {
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private final S3Service s3Service;

    @PostMapping("/api/file-upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        // 1. 타입 확인. 타입이 jpg, jpeg, png, gif,  webp 가 아닐 경우 ResponseEntity.badRequest
        if (!isSupportedContentType(file.getContentType())) {
            return ResponseEntity.badRequest().body("Unsupported file type. Please upload a JPG, JPEG, PNG, GIF, or WebP image.");
        }

        // 2. 파일 용량 확인. 최대 사이즈: 5mb. 만약 5mb 넘을 시 ResponseEntity.badRequest
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().body("File size exceeds the limit of 5MB.");
        }

        try {
            // 3. s3 에 업로드 & location return
            S3PostImage postImage = s3Service.uploadFile(file);
            return ResponseEntity.ok(Map.of("location", postImage.getUrl()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("처리 중 에러 발생");
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return ALLOWED_CONTENT_TYPES.contains(contentType);
    }
}
