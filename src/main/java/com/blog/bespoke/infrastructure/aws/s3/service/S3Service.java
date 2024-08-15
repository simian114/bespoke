package com.blog.bespoke.infrastructure.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.banner.S3BannerImage;
import com.blog.bespoke.domain.model.banner.S3BannerImageType;
import com.blog.bespoke.domain.model.post.S3PostImage;
import com.blog.bespoke.domain.model.user.S3UserAvatar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3Client;

    private final String bannerFolder = "banner/";

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.bucket-url}")
    private String bucketUrl;

    /**
     * 큰 용량의 파일은 어떻게? chunked 요청은 어떻게 해야할까?
     * keyName 은 upload 되는 filename 이다. random generated 되어야한다.
     */
    public S3PostImage uploadFile(MultipartFile file) throws IOException {
        // TODO: db 에도 저장해야함
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filename = UUID.randomUUID().toString() + ext;
        String fileUrl = bucketUrl + filename;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        PutObjectResult putObjectResult = s3Client.putObject(bucketName, filename, file.getInputStream(), metadata);
        S3PostImage postImage = S3PostImage.builder()
                .url(fileUrl)
                .filename(filename)
                .size(file.getSize())
                .originalFilename(originalFilename)
                .build();
        return postImage;
    }

    public S3UserAvatar uploadAvatar(MultipartFile file) {
        // TODO: db 에도 저장해야함
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filename = UUID.randomUUID().toString() + ext;
        String fileUrl = bucketUrl + filename;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            PutObjectResult putObjectResult = s3Client.putObject(bucketName, filename, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND.INTERNAL_SERVER_ERROR);
        }
        return S3UserAvatar.builder()
                .url(fileUrl)
                .filename(filename)
                .size(file.getSize())
                .originalFilename(originalFilename)
                .build();
    }

    public S3BannerImage uploadBannerImage(MultipartFile file, S3BannerImageType type) {
        // 폴더
        // TODO: db 에도 저장해야함
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filename = UUID.randomUUID().toString() + ext;
        String fileUrl = bucketUrl + bannerFolder + filename;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            PutObjectResult putObjectResult = s3Client.putObject(bucketName, filename, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND.INTERNAL_SERVER_ERROR);
        }
        return S3BannerImage.builder()
                .url(fileUrl)
                .filename(filename)
                .size(file.getSize())
                .originalFilename(originalFilename)
                .type(type)
                .build();
    }


    public S3Object getFile(String keyName) {
        return s3Client.getObject(bucketName, keyName);
    }
}
