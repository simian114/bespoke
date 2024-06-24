package com.blog.bespoke.domain.model.user.role;

import com.blog.bespoke.domain.model.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@IdClass(UserRoleId.class)
@Entity
@Table(name = "user_role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;
}
