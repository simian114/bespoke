package com.blog.bespoke.application.usecase;

import com.blog.bespoke.BespokeApplication;
import com.blog.bespoke.application.dto.request.CategoryCreateRequestDto;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.usecase.user.UserCategoryUseCase;
import com.blog.bespoke.application.usecase.user.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = BespokeApplication.class)
public class UserCategoryUseCaseTest {

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private UserCategoryUseCase userCategoryUseCase;

//    @Test
//    @DisplayName("category 생성 테스트")
    void test () {
        // given
        UserResponseDto user = userUseCase.signup(
                UserSignupRequestDto.builder().nickname("nickname").name("name").password("password").email("email@gmail.com").build()
        );
        CategoryCreateRequestDto requestDto = new CategoryCreateRequestDto();
        requestDto.setDescription("description");
        requestDto.setName("name");

        // when
        UserResponseDto category = userCategoryUseCase.createCategory(requestDto, user.getId());
        // then
        System.out.println(category);

    }
}
