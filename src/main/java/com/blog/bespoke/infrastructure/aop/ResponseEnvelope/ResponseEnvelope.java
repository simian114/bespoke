package com.blog.bespoke.infrastructure.aop.ResponseEnvelope;

public record ResponseEnvelope<T>(T data, String message, int status) {
}
