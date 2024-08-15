package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BannerForm extends TimeStamp {
    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_form_id")
    private Long id;
    private String result;

    @Enumerated(EnumType.STRING)
    private BannerFormStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String bannerSnapshot;

    /**
     * bannerForm 은 신청한 순간의 BannerType 을 가지고 있어야한다.
     * db 에서 이 값을 가져올 때 자동으로 obj 으로 만들어서 객체로 관리해아한다.
     */
    @Transient
    private Banner bannerObj;

    /**
     * 사실상 이건 쓰이지 않는다. 왜냐면 string 으로 저장된 데이터를 사용할거기때문.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Banner banner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime auditedAt;

    /**
     * db 에 저장할 때
     */
    @PrePersist
    @PreUpdate
    private void snapShotBanner() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.bannerSnapshot = objectMapper.writeValueAsString(bannerObj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * db 에서 가져올 때
     */
    @PostLoad
    private void setBannerObj() {
        try {
            this.bannerObj = objectMapper.readValue(this.bannerSnapshot, Banner.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
