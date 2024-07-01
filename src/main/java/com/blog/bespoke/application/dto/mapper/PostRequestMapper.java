package com.blog.bespoke.application.dto.mapper;

import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.domain.model.post.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostRequestMapper {
    PostRequestMapper INSTANCE = Mappers.getMapper(PostRequestMapper.class);

    Post toDomain(PostCreateRequestDto requestDto);
}
