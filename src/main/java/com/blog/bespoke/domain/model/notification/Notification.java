package com.blog.bespoke.domain.model.notification;

import com.blog.bespoke.domain.model.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.IOException;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private boolean readed;

    private Long refId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    // db 에는 이렇게 저장됨
    @Column(columnDefinition = "json")
    private String extra;

    // 실제 코드에서는 아래의 extraInfo 를 사용하도록한다.
    @Transient
    private ExtraInfo extraInfo;

    /**
     * db 에서 가져올 때
     */
    @PostLoad
    public void loadExtraInfo() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
             this.extraInfo = objectMapper.readValue(extra, ExtraInfo.class);
        } catch (IOException e) {
            this.extraInfo = null;
        }
    }

    /**
     * db 에 저장할 때
     */
    @PrePersist
    @PreUpdate
    private void saveExtraInfo() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.extra = objectMapper.writeValueAsString(extraInfo);
        } catch (IOException e) {
            this.extra = "";
        }
    }


    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    /**
     * recipient 와 publisher 를 넣어줄 때
     * 굳이 user = userRepository.findById(id) 로 가져올 필요 없이
     * User.builder.id(id).build() 로 생성하고
     * notification.save() 에 넣어도 될듯?
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private User publisher;

    @Transient
    public String getContent() {
        return "publisher is " + extraInfo.getPublisher() + " and recipient is " + extraInfo.getRecipient() + " and type is " + type.name();
    }

    public enum NotificationType {
        POST_PUBLISHED,
        COMMENT_CREATED,
        POST_LIKE,
        COMMENT_LIKE,
        FOLLOW,
    }
}
