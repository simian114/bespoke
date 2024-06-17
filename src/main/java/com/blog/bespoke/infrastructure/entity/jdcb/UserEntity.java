package com.blog.bespoke.infrastructure.entity.jdcb;

import com.blog.bespoke.domain.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "users")
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @Column("user_id")
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

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
