package com.blog.bespoke.infrastructure.repository.banner;

import com.blog.bespoke.domain.model.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerJpaRepository extends JpaRepository<Banner, Long> {
}
