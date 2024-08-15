package com.blog.bespoke.infrastructure.repository.banner;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.banner.Banner;
import com.blog.bespoke.domain.model.banner.BannerSearchCond;
import com.blog.bespoke.domain.model.banner.QBanner;
import com.blog.bespoke.domain.repository.banner.BannerRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepository {
    private final JPAQueryFactory queryFactory;
    private final BannerJpaRepository bannerJpaRepository;


    @Override
    public Banner save(Banner banner) {
        return bannerJpaRepository.save(banner);
    }

    @Override
    public Optional<Banner> findById(Long id) {
        return bannerJpaRepository.findById(id);
    }

    @Override
    public Banner getById(Long id) throws BusinessException {
        return bannerJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    @Override
    public void delete(Banner banner) {
        bannerJpaRepository.delete(banner);
    }

    @Override
    public void deleteById(Long id) {
        bannerJpaRepository.deleteById(id);
    }

    @Override
    public Page<Banner> search(BannerSearchCond cond) {
        Pageable pageable = cond.getPageable();

        JPAQuery<Banner> jpaQuery = query(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        jpaQuery.orderBy(QBanner.banner.createdAt.desc());
        List<Banner> banners = jpaQuery.fetch();

        Long totalSize = countQuery(cond).fetch().get(0);

        return PageableExecutionUtils.getPage(banners, pageable, () -> totalSize);

    }

    private JPAQuery<Banner> query(BannerSearchCond cond) {
        return queryFactory.select(QBanner.banner)
                .from(QBanner.banner)
                .where(advertiserIdEq(cond),
                        advertiserNicknameEq(cond)
                );
    }

    private JPAQuery<Long> countQuery(BannerSearchCond cond) {
        return queryFactory.select(Wildcard.count)
                .from(QBanner.banner)
                .where(advertiserIdEq(cond),
                        advertiserNicknameEq(cond)
                );
    }


    private BooleanExpression advertiserIdEq(BannerSearchCond cond) {
        if (cond == null || cond.getAdvertiserId() == null) {
            return null;
        }
        return QBanner.banner.advertiser.id.eq(cond.getAdvertiserId());
    }

    private BooleanExpression advertiserNicknameEq(BannerSearchCond cond) {
        if (cond == null || cond.getAdvertiserNickname() == null) {
            return null;
        }
        return QBanner.banner.advertiser.nickname.eq(cond.getAdvertiserNickname());
    }

}
