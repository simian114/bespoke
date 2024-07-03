package com.blog.bespoke.application.event.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishPostEvent {
    private Long postId;
    private Long authorId;
    private String title;
    private String description;
}
