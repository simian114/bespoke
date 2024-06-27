package com.blog.bespoke.domain.model.user;

import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.follow.Follow;
import com.blog.bespoke.domain.model.user.role.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Table(name = "users")
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends TimeStamp {
    // --- relation
    @OneToMany(mappedBy = "followerId", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Follow> followers = new HashSet<>();
    @OneToMany(mappedBy = "followingId", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Follow> followings = new HashSet<>();
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> roles;
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.INACTIVE;
    private LocalDateTime bannedUntil;

    @JsonIgnore
    public List<String> getRolesAsString() {
        return roles.stream()
                .map(role -> role.getRole().getCode().name())
                .toList();
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

    // --- transient value

    // --- domain logic

    @JsonIgnore
    @Transient
    public void addRole(UserRole role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    @JsonIgnore
    @Transient
    public void changePassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    @Transient
    public boolean isActive() {
        return status == STATUS.ACTIVE;
    }

    @JsonIgnore
    @Transient
    public void activate() {
        status = STATUS.ACTIVE;
    }

    @JsonIgnore
    @Transient
    public void deActivate() {
        status = STATUS.INACTIVE;
    }


    public enum STATUS {
        INACTIVE,
        ACTIVE
    }
}
