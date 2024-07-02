package com.blog.bespoke.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NoticeUseCase {
    //
    public void noticeToFollowers() {
        log.info("notice to followers~");
    }

    // userId
    public void noticeToUser() {
        log.info("특정 유저에게만 생성되는 알림");
    }
}
