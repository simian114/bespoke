package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    private String name;
    private String title;
    private String subTitle;
    private String content;
    private String link;

    @Enumerated(EnumType.STRING)
    private BannerUiType uiType;

    private String backgroundColor;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User advertiser;

    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<S3BannerImage> bannerImages;

    /**
     * Banner 는 완전 종료 되지 않은 상태의 bannerForm 이 존재하는 한 게시 요청을 하지 못한다.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BannerForm> bannerForms;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    protected LocalDateTime updatedAt;

    @JsonIgnore
    protected LocalDateTime deletedAt;

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

}
