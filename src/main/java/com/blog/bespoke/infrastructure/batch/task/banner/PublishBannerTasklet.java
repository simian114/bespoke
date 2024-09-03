package com.blog.bespoke.infrastructure.batch.task.banner;

import com.blog.bespoke.application.usecase.banner.BannerFormUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j(topic = "BATCH_PUBLISH_BANNER")
@Component
@RequiredArgsConstructor
public class PublishBannerTasklet implements Tasklet {
    private final BannerFormUseCase bannerFormUseCase;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        bannerFormUseCase.publishPaymentCompleted();
        return RepeatStatus.FINISHED;
    }

}









