package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Banner extends TimeStamp {
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User advertiser;

    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<S3BannerImage> bannerImages;

    /**
     * Banner 는 완전 종료 되지 않은 상태의 bannerForm 이 존재하는 한 게시 요청을 하지 못한다.
     */
    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BannerForm> bannerForms;

}
