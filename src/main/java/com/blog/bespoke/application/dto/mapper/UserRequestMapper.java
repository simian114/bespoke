package com.blog.bespoke.application.dto.mapper;

import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.domain.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);

    User toDomain(UserSignupRequestDto requestDto);
}
