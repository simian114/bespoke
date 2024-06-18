package com.blog.bespoke.infrastructure.entity.jpa;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.infrastructure.entity.jpa.common.TimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Table(name = "users")
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends TimeStamp {
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

    // TODO: toDomain
    public User toDomain() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .name(name)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }
}
