package com.blog.bespoke.domain.repository.banner;

import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.BannerSearchCond;
import org.springframework.data.domain.Page;

public interface BannerSearchRepository {

    Page<Banner> search(BannerSearchCond cond);
}
