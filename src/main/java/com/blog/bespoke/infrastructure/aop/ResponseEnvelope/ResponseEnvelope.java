package com.blog.bespoke.infrastructure.aop.ResponseEnvelope;

public record ResponseEnvelope<T>(T data, String message, int status) {
    @Override
    public String toString() {
        return String.format("""
                {
                  "data": %s,
                  "message": %s,
                  "status": %d,
                }
                """, data, message, status);
    }
}
