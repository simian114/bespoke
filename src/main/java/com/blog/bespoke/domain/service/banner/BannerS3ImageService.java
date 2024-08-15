package com.blog.bespoke.domain.service.banner;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class BannerS3ImageService {
    // 지원하는 포맷인지 확인
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public boolean checkCanUpload(MultipartFile file) {
        if (!isSupportedContentType(file.getContentType())) {
            throw new BusinessException(ErrorCode.UNSUPPORTED_IMAGE);
        }

        // 2. 파일 용량 확인. 최대 사이즈: 5mb. 만약 5mb 넘을 시 ResponseEntity.badRequest
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.COVER_IMAGE_LIMIT_SIZE);
        }
        return true;
    }

    private boolean isSupportedContentType(String contentType) {
        return ALLOWED_CONTENT_TYPES.contains(contentType);
    }


}
