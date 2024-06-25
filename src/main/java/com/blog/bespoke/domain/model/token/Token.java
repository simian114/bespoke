package com.blog.bespoke.domain.model.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Token {
    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Long refId;

    @Enumerated(EnumType.STRING)
    private RefType refType;

    private LocalDateTime expiredAt;

    private String code;

    public enum Type {
        EMAIL_VALIDATION,
        REFRESH_TOKEN
    }

    public enum RefType {
        USER,
        POST
    }
}
