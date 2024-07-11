package com.blog.bespoke.application.dto.mapper;

import com.blog.bespoke.application.dto.request.CategoryCreateRequestDto;
import com.blog.bespoke.domain.model.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryRequestMapper {
    CategoryRequestMapper INSTANCE = Mappers.getMapper(CategoryRequestMapper.class);

    Category toDomain(CategoryCreateRequestDto requestDto);
}
