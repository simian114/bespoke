package com.blog.bespoke.infrastructure.aop.logtrace;

public record TraceStatus(TraceNode traceNode, Long startTimeMs, String message) {
}
