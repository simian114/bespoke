package com.blog.bespoke.infrastructure.batch;

import com.blog.bespoke.infrastructure.batch.task.banner.EndBannerTasklet;
import com.blog.bespoke.infrastructure.batch.task.banner.PublishBannerTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchJobs {
    private final PublishBannerTasklet publishBannerTasklet;
    private final EndBannerTasklet endBannerTasklet;
    private final PlatformTransactionManager transactionManager;

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

        return new JobBuilder("dailyBannerJob", jobRepository)
                .start(endBannerStep)
                .next(publishBannerStep)
                .build();
    }

}
