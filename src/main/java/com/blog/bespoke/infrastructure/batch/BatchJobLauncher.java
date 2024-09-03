package com.blog.bespoke.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

@Slf4j(topic = "job_launcher")
@Component
@RequiredArgsConstructor
public class BatchJobLauncher {
    private final JobLauncher jobLauncher;

    private final Job dailyBannerJob;

    public void launchDailyBannerJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(dailyBannerJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }


}
