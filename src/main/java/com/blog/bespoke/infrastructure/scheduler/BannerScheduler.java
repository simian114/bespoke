package com.blog.bespoke.infrastructure.scheduler;

import com.blog.bespoke.infrastructure.batch.BatchJobLauncher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "banner scheduler")
@Component
@RequiredArgsConstructor
public class BannerScheduler {
    private final BatchJobLauncher jobLauncher;

    /**
     * 매일 새벽 3시에 실행됨.
     * 새벽 3시로 지정한 이유는 이 시간이 가장 사용자가 적을거라 판단했기 때문.
     */
    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Seoul")
    public void run() {
        jobLauncher.launchDailyBannerJob();
    }

}
