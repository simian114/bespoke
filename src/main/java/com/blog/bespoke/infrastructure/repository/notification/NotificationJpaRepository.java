package com.blog.bespoke.infrastructure.repository.notification;

import com.blog.bespoke.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.recipient.id = :recipientId")
    List<Notification> findAllByRecipientId(@Param("recipientId") Long recipientId);
}
