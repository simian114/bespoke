package com.blog.bespoke.infrastructure.repository.notification;

import com.blog.bespoke.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.recipient.id = :recipientId")
    List<Notification> findAllByRecipientId(@Param("recipientId") Long recipientId);

    // NOTE: @Modifying 를 사용하기 위해선 반드시 트랜잭션이 사용돼야한다.
    @Modifying
    @Query("update Notification n set n.readed = true where n.id = :notificationId")
    void read(@Param("notificationId") Long notificationId);
}
