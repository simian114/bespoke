package com.blog.bespoke.infrastructure.batch.task.banner;

import com.blog.bespoke.application.memorhcache.MemoryCacheKeyManager;
import com.blog.bespoke.infrastructure.repository.redis.RedisCacheManager;
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
public class InvalidateBannerCacheTasklet implements Tasklet {
    private final RedisCacheManager redisCacheManager;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        MemoryCacheKeyManager.Banner.getAllManagedKeys().forEach(redisCacheManager::invalidate);
        return RepeatStatus.FINISHED;
    }

}









