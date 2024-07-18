package com.blog.bespoke.presentation.web.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SseController {
    //    private final SseEmitter sseEmitter;
    private final Map<Long, SseEmitter> sseEmitterMap = new HashMap<>();

    @GetMapping(value = "/noti/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter >subscribe(@PathVariable("userId") Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitterMap.put(userId, emitter);
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connect")
                            .data("connected!")
            );
        } catch (IOException e) {
            log.error("sse connect error!");
        }
        return ResponseEntity.ok(emitter);
    }

}
