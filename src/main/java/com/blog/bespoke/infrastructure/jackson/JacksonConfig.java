package com.blog.bespoke.infrastructure.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        // 타임스탬프 대신 ISO 8601 형식으로 직렬화하도록 설정합니다.
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

}
