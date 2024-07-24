package com.blog.bespoke.infrastructure.repository.notification;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.notification.Notification;
import com.blog.bespoke.domain.model.notification.NotificationSearchCond;
import com.blog.bespoke.domain.repository.notification.NotificationRepository;
import com.querydsl.core.types.Projections;
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

import static com.blog.bespoke.domain.model.notification.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final JPAQueryFactory queryFactory;
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public Notification getById(Long id) throws BusinessException {
        return notificationJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    @Override
    public List<Notification> findAllByUserId(Long recipientId) {
        return notificationJpaRepository.findAllByRecipientId(recipientId);
    }

    @Override
    public void saveAll(Iterable<Notification> notifications) {
        notificationJpaRepository.saveAll(notifications);
    }

    @Override
    public void readNotification(Long notificationId) {
        notificationJpaRepository.read(notificationId);
    }

    // querydsl 사용
    @Override
    public Page<Notification> search(NotificationSearchCond cond) {
        Pageable pageable = PageRequest.of(cond.getPage() == null ? 0 : cond.getPage(), cond.getPageSize() == null ? 20 : cond.getPageSize());
        JPAQuery<Notification> jpaQuery = query(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notification.createdAt.desc());
        List<Notification> notifications = jpaQuery.fetch();
        // NOTE: querydsl 로 생성된 객체는 PostLoad가 실행되지 않음
        for (Notification noti : notifications) {
            noti.loadExtraInfo();
        }
        Long totalSize = countQuery(cond).fetch().get(0);

        return PageableExecutionUtils.getPage(notifications, pageable, () -> totalSize);
    }

    private JPAQuery<Notification> query(NotificationSearchCond cond) {
        JPAQuery<Notification> query = queryFactory.select(
                        Projections.fields(
                                Notification.class,
                                notification.refId,
                                notification.id,
                                notification.createdAt,
                                notification.type,
                                notification.extra,
                                notification.readed

//                                Projections.fields(
//                                        User.class,
//                                        notification.publisher.id,
//                                        notification.publisher.nickname,
//                                        notification.publisher.name,
//                                        notification.publisher.email
//                                ).as("publisher"),
//                                Projections.fields(
//                                        User.class,
//                                        notification.recipient.id,
//                                        notification.recipient.nickname,
//                                        notification.recipient.name,
//                                        notification.recipient.email
//                                ).as("recipient")
                        )
                )
                .from(notification)
                .where(
                        typeEq(cond),
                        recipientIdEq(cond),
                        readedEq(cond)
                );

        return query;
    }

    private JPAQuery<Long> countQuery(NotificationSearchCond cond) {
        JPAQuery<Long> query = queryFactory.select(Wildcard.count)
                .from(notification)
                .where(
                        typeEq(cond),
                        recipientIdEq(cond),
                        readedEq(cond)
                );
        return query;
    }

    private BooleanExpression typeEq(NotificationSearchCond cond) {
        if (cond != null && cond.getType() != null) {
            return notification.type.eq(cond.getType());
        }
        return null;
    }

    private BooleanExpression recipientIdEq(NotificationSearchCond cond) {
        if (cond != null && cond.getRecipientId() != null) {
            return notification.recipient.id.eq(cond.getRecipientId());
        }
        return null;
    }

    private BooleanExpression readedEq(NotificationSearchCond cond) {
        if (cond != null && cond.getReaded() != null) {
            return notification.readed.eq(cond.getReaded());
        }
        return null;
    }
}
