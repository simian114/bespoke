package com.blog.bespoke.domain.model.user;

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
public class UserCountInfo {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private Long followerCount = 0L;

    @Column(nullable = false)
    private Long followingCount = 0L;

    @Column(nullable = false)
    private Long publishedPostCount = 0L;

    @Column(nullable = false)
    private Long likePostCount = 0L;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // domain logic

    public void incrementFollower() {
        followerCount += 1;
    }

    public void incrementFollowing() {
        followingCount += 1;
    }

    public void decrementFollowing() {
        followingCount -= 1;
    }

    public void decrementFollower() {
        followerCount -= 1;
    }
}
