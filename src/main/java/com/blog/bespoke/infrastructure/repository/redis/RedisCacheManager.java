package com.blog.bespoke.infrastructure.repository.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "redis")
@Component
@RequiredArgsConstructor
public class RedisCacheManager {
    // NOTE: 기본은 하루
    private static final Long TtlAsMilliSeconds = (long) (1000 * 60 * 60 * 24);
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T get(String key, TypeReference<T> typeReference ) {
        try {
            Object object = redisTemplate.opsForValue().get(key);
            if (object == null) {
                return null;
            }
            return objectMapper.convertValue(object, typeReference);
        } catch (Exception e) {
            return null;
        }
    }


    public <T> T get(String key, Class<T> clazz) {
        try {
            Object object = redisTemplate.opsForValue().get(key);
            if (object == null) {
                return null;
            }
            return objectMapper.convertValue(object, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public void invalidateByPattern(String patternKey) {
        Set<String> keys = redisTemplate.keys(patternKey);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void invalidate(String key) {
        redisTemplate.delete(key);
    }

    public void set(String key, Object value) {
        templateSet(key, value, TtlAsMilliSeconds, TimeUnit.MILLISECONDS);
    }

    public void set(String key, Object value, Long ttlAsMinute) {
        templateSet(key, value, ttlAsMinute, TimeUnit.MINUTES);
    }

    private void templateSet(String key, Object value, Long ttl, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl, unit);
        } catch (Exception e) {
            log.error(key, " redis 직렬화 실패", e);
        }
    }

}
