package com.blog.bespoke.presentation.web.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BasicErrorResponse {
    private LocalDateTime timestamp;
    private String error;
    private String path;

    @Override
    public String toString() {
        return String.format("""
                        {
                            "timestamp": "%s",
                            "error": "%s",
                            "path": "%s"
                        }
                        """,
                LocalDateTime.now(),
                this.error,
                this.path
        );
    }

}
