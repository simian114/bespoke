package com.blog.bespoke.domain.repository.banner;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.domain.model.banner.BannerForm;

import java.util.Optional;

public interface BannerFormRepository extends BannerFormSearchRepository {
    BannerForm save(BannerForm bannerForm);

    Optional<BannerForm> findById(Long id);

    BannerForm getById(Long id) throws BusinessException;

    void delete(BannerForm bannerForm);

    void deleteById(Long id);

}
