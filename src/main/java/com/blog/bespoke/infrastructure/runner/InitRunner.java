package com.blog.bespoke.infrastructure.runner;

import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.usecase.post.PostUseCase;
import com.blog.bespoke.application.usecase.user.UserFollowUseCase;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.infrastructure.repository.post.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PostUseCase postUseCase;
    private final PostJpaRepository postJpaRepository;
    private final UserFollowUseCase userFollowUseCase;

    // TODO: 개발환경에서만 실행하려면 어떻게 해야할까?
    @Override
    public void run(String... args) throws Exception {
        long postCount = postJpaRepository.count();

        try {
            initFollow(1L, 2L);
            initFollow(1L, 3L);
            initFollow(1L, 4L);
            initFollow(1L, 5L);
            initFollow(1L, 6L);
            initFollow(1L, 7L);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            if (postCount > 200) {
                System.out.println("이미 충분함!");
                return;
            }
            initPosts(1L, 10);
            initPosts(2L, 10);
            initPosts(3L, 10);
            initPosts(4L, 10);
            initPosts(5L, 10);
            initPosts(6L, 10);
            initPosts(7L, 10);
            initPosts(8L, 10);
            initPosts(9L, 10);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    private void initPosts(Long userId, int count) {
        try {
            User user = userRepository.getById(userId);
            for (int i = 0; i < count; i++) {
                PostCreateRequestDto dto = PostCreateRequestDto.builder().title("title").description("description").content("content").status(Post.Status.PUBLISHED).build();
                postUseCase.write(dto, user);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void initFollow(Long userId1, Long userId2) {
        try {
            User user1 = userRepository.getById(userId1);
            User user2 = userRepository.getById(userId2);
            userFollowUseCase.follow(user1.getId(), user2);
            userFollowUseCase.follow(user2.getId(), user1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
