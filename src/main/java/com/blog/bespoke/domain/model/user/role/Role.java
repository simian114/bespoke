package com.blog.bespoke.domain.model.user.role;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Table(name = "role")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    @Id
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Code code;

    public enum Code {
        USER,
        ADMIN
    }
}
