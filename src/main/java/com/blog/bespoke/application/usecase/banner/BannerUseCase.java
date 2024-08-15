package com.blog.bespoke.application.usecase.banner;

import com.blog.bespoke.application.dto.request.BannerCreateRequestDto;
import com.blog.bespoke.application.dto.response.BannerResponseDto;
import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.S3BannerImage;
import com.blog.bespoke.domain.model.banner.S3BannerImageType;
import com.blog.bespoke.domain.model.post.S3PostImage;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.banner.BannerRepository;
import com.blog.bespoke.domain.service.banner.BannerS3ImageService;
import com.blog.bespoke.infrastructure.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BannerUseCase {
    private final BannerRepository bannerRepository;
    private final S3Service s3Service;
    private final BannerS3ImageService bannerS3ImageService;

    public BannerResponseDto createBanner(BannerCreateRequestDto requestDto, User currentUser) {

        // S3 에 올라간거지, db 에는 생성되지 않음. 댕글링.
        S3BannerImage pcBannerImage = checkAndCreateS3BannerImage(requestDto.getPcImage(), S3BannerImageType.PC);
        S3BannerImage mobileBannerImage = checkAndCreateS3BannerImage(requestDto.getPcImage(), S3BannerImageType.MOBILE);

        // TODO: upload banner image to s3 and get S3BannerImage (PC / MOBILE)
        Banner banner = requestDto.toModel(currentUser, pcBannerImage, mobileBannerImage);

        Banner savedBanner = bannerRepository.save(banner);

        return BannerResponseDto.from(savedBanner);
    }

    private S3BannerImage checkAndCreateS3BannerImage(MultipartFile file, S3BannerImageType type) {

        bannerS3ImageService.checkCanUpload(file);

        S3BannerImage unSavedImage;
        unSavedImage = s3Service.uploadBannerImage(file, type);

        return unSavedImage;
    }


}
