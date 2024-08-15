package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.file.S3File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "s3_banner_image")
@SuperBuilder
public class S3BannerImage extends S3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s3_banner_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Banner banner;

    @Enumerated(EnumType.STRING)
    private S3BannerImageType type;

    public void setType(S3BannerImageType type) {
        this.type = type;
    }

    @Transient
    public boolean isPCImage() {
        return this.type == S3BannerImageType.PC;
    }

    @Transient
    public boolean isMobileImage() {
        return this.type == S3BannerImageType.MOBILE;
    }

    public enum Type {
        CONTENT, // 게시글 내용에 들어가는 이미지
        COVER // 커버
    }
}
