package com.blog.bespoke.domain.model.user;

import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.follow.Follow;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.role.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@DynamicInsert
@DynamicUpdate
@Getter
@Table(name = "users")
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String nickname;
    private String name;

    // --- relation
    @OneToMany(mappedBy = "followingId", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "followerId", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Category> categories = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserCountInfo userCountInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> roles;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;
    private LocalDateTime bannedUntil;

    @JsonIgnore
    public List<String> getRolesAsString() {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(role -> role.getRole().getCode().name())
                .toList();
    }

    // --- 연관관계 편의 메서드

    // --- transient value

    // --- domain logic
    public void init() {
        this.deActivate();
        this.userCountInfo = UserCountInfo.builder().user(this).build();
    }

    public void setProfile(UserProfile profile) {
        this.userProfile = profile;
        profile.setUser(this);
    }

    public void follow(Long followingId) {
        if (followings == null) {
            followings = new HashSet<>();
        }
        followings.add(
                Follow.builder()
                        .followerId(id)
                        .followingId(followingId)
                        .build()
        );
    }

    public void unfollow(Long followingId) {
        if (followings == null) {
            followings = new HashSet<>();
        }
        followings.stream()
                .filter(f -> Objects.equals(f.getFollowingId(), followingId))
                .findFirst().ifPresent(follow -> followings.remove(follow));

    }

    public void addFollower(Long followerId) {
        if (followers == null) {
            followers = new HashSet<>();
        }
        followers.add(Follow.builder()
                .followerId(followerId)
                .followingId(id)
                .build()
        );
    }

    public void removeFollower(Long followerId) {
        if (followers == null) {
            followers = new HashSet<>();
        }
        followers.stream()
                .filter(f -> Objects.equals(f.getFollowerId(), followerId))
                .findFirst().ifPresent(follow -> followers.remove(follow));
    }


    @JsonIgnore
    @Transient
    public void addRole(UserRole role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        role.setUser(this);
    }

    @JsonIgnore
    @Transient
    public void changePassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    @Transient
    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    @Transient
    public boolean isAdmin() {
        return false;
    }


    @JsonIgnore
    @Transient
    public void activate() {
        status = Status.ACTIVE;
    }

    @JsonIgnore
    @Transient
    public void deActivate() {
        status = Status.INACTIVE;
    }

    @Transactional
    @Transient
    public void addCategory(Category category) {
        if (categories == null) {
            categories = new LinkedHashSet<>();
        }
        categories.add(category);
        category.setUser(this);
    }

    public void removeCategory(Long categoryId) {
        if (categories == null) {
            return;
        }
        categories.stream()
                .filter(c -> c.getId().equals(categoryId))
                .findFirst().ifPresent(category -> categories.remove(category));
    }

    /**
     * name과 introduce만 변경가능. nickname 은 변경 불가능함
     */
    public void update(UserUpdateCmd cmd) {
        if (cmd.getName() != null && !cmd.getName().isBlank()) {
            name = cmd.getName();
        }
        if (cmd.getIntroduce() != null && !cmd.getIntroduce().isBlank() && this.userProfile != null) {
            this.userProfile.setIntroduce(cmd.getIntroduce());
        }
    }


    public enum Status {
        INACTIVE,
        ACTIVE
    }
}
