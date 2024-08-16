package com.blog.bespoke.application.usecase.banner;

import com.blog.bespoke.domain.repository.banner.BannerFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerFormUseCase {
    private final BannerFormRepository bannerFormRepository;

}
