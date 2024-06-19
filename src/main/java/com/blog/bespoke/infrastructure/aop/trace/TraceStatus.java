package com.blog.bespoke.infrastructure.aop.trace;

public record TraceStatus(TraceId traceId, Long startTimeMs, String message) {
}
