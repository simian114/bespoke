package com.blog.bespoke.domain.model.notification;

import lombok.*;

import java.util.Optional;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraInfo {
    private String publisher; // 알림을 생성 한 주체(user) 의 nickname
    private String recipient; // 알림을 받는 유저 의 nickname
    private String postTitle; // optional. 게시글 관련 알림일 경우 넣기
}
