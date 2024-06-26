package com.blog.bespoke.infrastructure.repository.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "redis")
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final Long TtlAsMilliSeconds = (long) (1000 * 60 * 60);

    public <T> T get(String key, Class<T> clazz) {
        Object object;
        try {
            object = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return null;
        }
        if (object == null) {
            return null;
        }
        return objectMapper.convertValue(object, clazz);
    }

    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value, TtlAsMilliSeconds, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error(key, " redis 직렬화 실패", e);
        }
    }
}
