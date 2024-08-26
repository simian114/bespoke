package com.blog.bespoke.domain.repository.banner;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormRelation;

import java.util.Optional;

public interface BannerFormRepository extends BannerFormSearchRepository {
    BannerForm save(BannerForm bannerForm);

    Optional<BannerForm> findById(Long id);
    Optional<BannerForm> findById(Long id, BannerFormRelation relation);

    BannerForm getById(Long id) throws BusinessException;
    BannerForm getById(Long id, BannerFormRelation relation) throws BusinessException;

    void delete(BannerForm bannerForm);

    void deleteById(Long id);

}
