package com.blog.bespoke.infrastructure.aop.ResponseEnvelope;

import com.blog.bespoke.infrastructure.web.filter.transaction.TrIdHolder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EnvelopeResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    private final String message;
    private final int status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String path;

    private final String trId;

    private EnvelopeResponse(Object data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
        this.trId = TrIdHolder.getTrId();
    }

    private EnvelopeResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.trId = TrIdHolder.getTrId();
    }

    public static ResponseEntity<?> envelope(Object data, HttpStatus status, String message) {
        return ResponseEntity
                .status(status == HttpStatus.NO_CONTENT ? HttpStatus.OK : status)
                .body(new EnvelopeResponse(data, status.value(), message));
    }

    public static ResponseEntity<?> envelope(HttpStatus status, String message, String path) {
        return ResponseEntity
                .status(status == HttpStatus.NO_CONTENT ? HttpStatus.OK : status)
                .body(new EnvelopeResponse(status.value(), message, path));
    }
}
