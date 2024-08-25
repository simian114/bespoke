package com.blog.bespoke.infrastructure.repository.banner;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormSearchCond;
import com.blog.bespoke.domain.model.banner.QBannerForm;
import com.blog.bespoke.domain.repository.banner.BannerFormRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BannerFormRepositoryImpl implements BannerFormRepository {
    private final JPAQueryFactory queryFactory;
    private final BannerFormJpaRepository bannerFormJpaRepository;

    @Override
    public BannerForm save(BannerForm bannerForm) {
        return bannerFormJpaRepository.save(bannerForm);
    }

    @Override
    public Optional<BannerForm> findById(Long id) {
        return bannerFormJpaRepository.findById(id);
    }

    @Override
    public BannerForm getById(Long id) throws BusinessException {
        return bannerFormJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    @Override
    public void delete(BannerForm bannerForm) {
        bannerFormJpaRepository.delete(bannerForm);
    }

    @Override
    public void deleteById(Long id) {
        bannerFormJpaRepository.deleteById(id);
    }

    @Override
    public Page<BannerForm> search(BannerFormSearchCond cond) {
        Pageable pageable = cond.getPageable();

        JPAQuery<BannerForm> jpaQuery = query(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        jpaQuery.orderBy(QBannerForm.bannerForm.createdAt.desc());
        List<BannerForm> bannerForms = jpaQuery.fetch();

        Long totalSize = countQuery(cond).fetch().get(0);

        return PageableExecutionUtils.getPage(bannerForms, pageable, () -> totalSize);
    }

    private JPAQuery<BannerForm> query(BannerFormSearchCond cond) {
        return queryFactory.select(QBannerForm.bannerForm)
                .from(QBannerForm.bannerForm)
                // bannerForm 은 굳이 join 할 필요 없음
                .leftJoin(QBannerForm.bannerForm.user)
                .where(
                        userIdEq(cond),
                        bannerIdEq(cond),
                        statusesIn(cond),
                        nicknameEq(cond),
                        dateAfter(cond),
                        dateBefore(cond),
                        uiTypeEq(cond)
                );
    }

    private JPAQuery<Long> countQuery(BannerFormSearchCond cond) {
        return queryFactory.select(Wildcard.count)
                .from(QBannerForm.bannerForm)
                .where(
                        userIdEq(cond),
                        bannerIdEq(cond),
                        statusesIn(cond),
                        nicknameEq(cond),
                        dateAfter(cond),
                        dateBefore(cond),
                        uiTypeEq(cond)
                );
    }

    private BooleanExpression dateAfter(BannerFormSearchCond cond) {
        if (cond == null || cond.getStartDate() == null) {
            return null;
        }
        return QBannerForm.bannerForm.startDate.after(cond.getStartDate());
    }

    private BooleanExpression dateBefore(BannerFormSearchCond cond) {
        if (cond == null || cond.getEndDate() == null) {
            return null;
        }
        return QBannerForm.bannerForm.endDate.before(cond.getEndDate());
    }

    private BooleanExpression uiTypeEq(BannerFormSearchCond cond) {
        if (cond == null || cond.getUiType() == null) {
            return null;
        }
        return QBannerForm.bannerForm.uiType.eq(cond.getUiType());
    }

    private BooleanExpression datesBetween(BannerFormSearchCond cond) {
        if (cond == null) {
            return null;
        }
        return QBannerForm.bannerForm.status.in(cond.getStatuses());
    }


    private BooleanExpression statusesIn(BannerFormSearchCond cond) {
        if (cond == null || cond.getStatuses() == null || cond.getStatuses().isEmpty()) {
            return null;
        }
        return QBannerForm.bannerForm.status.in(cond.getStatuses());
    }

    private BooleanExpression bannerIdEq(BannerFormSearchCond cond) {
        if (cond == null || cond.getBannerId() == null) {
            return null;
        }
        return QBannerForm.bannerForm.banner.id.eq(cond.getBannerId());
    }

    private BooleanExpression nicknameEq(BannerFormSearchCond cond) {
        if (cond == null || cond.getNickname() == null || cond.getNickname().isBlank()) {
            return null;
        }
        return QBannerForm.bannerForm.user.nickname.eq(cond.getNickname());
    }


    private BooleanExpression userIdEq(BannerFormSearchCond cond) {
        if (cond == null || cond.getUserId() == null) {
            return null;
        }
        return QBannerForm.bannerForm.user.id.eq(cond.getUserId());
    }


}
