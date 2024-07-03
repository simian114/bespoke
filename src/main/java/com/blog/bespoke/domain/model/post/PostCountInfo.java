package com.blog.bespoke.domain.model.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class PostCountInfo {
    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(nullable = false)
    private long likeCount;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long commentCount;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;
}
