package com.blog.bespoke.application.dto.request.search;

public interface CommonSearchRequestDto<T> {
    String toString();

    T toModel();

}
