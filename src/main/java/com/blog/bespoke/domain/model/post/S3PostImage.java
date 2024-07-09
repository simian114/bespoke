package com.blog.bespoke.domain.model.post;

import com.blog.bespoke.domain.model.file.S3File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "s3_post_image")
@DynamicInsert
@DynamicUpdate
public class S3PostImage extends S3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s3_post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
