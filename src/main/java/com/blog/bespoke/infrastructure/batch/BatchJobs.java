package com.blog.bespoke.infrastructure.batch;

import com.blog.bespoke.infrastructure.batch.task.banner.PublishBannerTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * job 을 정의함.
 * 재사용해야하는 step 이 나온다면 그때 step 분리하기. 현재는 그럴 일 없음
 */
@Configuration
@RequiredArgsConstructor
public class BatchJobs {
    // transaction manager
    private final PlatformTransactionManager transactionManager;

    // banner task
    private final Tasklet publishBannerTasklet;
    private final Tasklet endBannerTasklet;
    private final Tasklet invalidateBannerCacheTasklet;

    /**
     * 매일 03:00 시에 실행하게 스케줄러 작업이 필요함
     */
    @Bean
    public Job dailyBannerJob(JobRepository jobRepository) {
        TaskletStep publishBannerStep = new StepBuilder("publishBannerStep", jobRepository)
                .tasklet(publishBannerTasklet, transactionManager)
                .build();

        TaskletStep endBannerStep = new StepBuilder("endBannerStep", jobRepository)
                .tasklet(endBannerTasklet, transactionManager)
                .build();

        TaskletStep invalidateCacheStep = new StepBuilder("invalidateCacheStep", jobRepository)
                .tasklet(invalidateBannerCacheTasklet, transactionManager)
                .build();

        return new JobBuilder("dailyBannerJob", jobRepository)
                .start(endBannerStep)
                .next(publishBannerStep)
                .next(invalidateCacheStep)
                .build();
    }

}
