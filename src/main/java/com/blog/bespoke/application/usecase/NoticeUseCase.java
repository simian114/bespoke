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
}
