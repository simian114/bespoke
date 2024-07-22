package com.blog.bespoke.domain.model.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ExtraInfo {
    private String publisher; // 알림을 생성 한 주체
    private String recipient; // 알림을 받는 유저
    private String postTitle; // optional. 게시글 관련 알림일 경우 넣기
}
