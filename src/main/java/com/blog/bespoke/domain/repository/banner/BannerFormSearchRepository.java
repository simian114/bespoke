package com.blog.bespoke.domain.repository.banner;

import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormSearchCond;
import org.springframework.data.domain.Page;

public interface BannerFormSearchRepository {
    Page<BannerForm> search(BannerFormSearchCond cond);
}
