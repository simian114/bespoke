package com.blog.bespoke.domain.model.user.role;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Table(name = "role")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CODE code;

    public enum CODE {
        USER,
        ADMIN
    }
}
