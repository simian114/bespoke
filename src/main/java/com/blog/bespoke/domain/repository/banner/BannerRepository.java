package com.blog.bespoke.domain.repository.banner;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.domain.model.banner.Banner;

import java.util.Optional;

public interface BannerRepository extends BannerSearchRepository {
    Banner save(Banner banner);

    // relation 을 만들까?
    Optional<Banner> findById(Long id);

    Banner getById(Long id) throws BusinessException;

    void delete(Banner banner);

    void deleteById(Long id);
}
