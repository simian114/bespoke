package com.blog.bespoke.domain.model.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ExtraInfo {
    private String publisher;
    private String recipient;
    private String postTitle;
}
