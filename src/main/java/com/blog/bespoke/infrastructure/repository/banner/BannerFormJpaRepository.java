package com.blog.bespoke.infrastructure.repository.banner;

import com.blog.bespoke.domain.model.banner.BannerForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerFormJpaRepository extends JpaRepository<BannerForm, Long> {
}
