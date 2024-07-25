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

//@Component
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
                PostCreateRequestDto dto = PostCreateRequestDto.builder().title("title").description("description").content("""
                        <p>안녕하세요 sanam 입니다.</p>
                        <h2>Bespoke 블로그 프로젝트를 만들게 된 이유</h2>
                        <p>개인 프로젝트로 블로글 플랫폼을 만들게 되었습니다.&nbsp; 수 많은 주제 가운데 블로그 플랫폼을 선택한 이유는 아래와 같습니다.</p>
                        <ol>
                        <li>블로그 플랫폼은 확장이 쉽다..</li>
                        <li>초반의 기획이 다른 서비스들보다 쉽다.</li>
                        </ol>
                        <p>확장이 쉽다라는건, 간단한 블로그지만 알림 / 채팅 / 광고 등 많은 서비스와 결합이 가능하다는 것이고 초반으 기획이 쉽다는 건 단순 블로그이기 때문에 기획이 어렵지 않다는 의미입니다.</p>
                        <p>즉, 첫 프로젝트로 시작하기에 이만한 주제가 없다고 생각했습니다.</p>
                        <p>물론 초반이 쉽다고 해서 개발이 쉬워서는 안된다고 생각합니다.</p>
                        <p>개발 시작 전 해당 프로젝트의 구조를 어떻게 가져갈지에 대해 많은 고민을 했고, 그 결과 <strong>도메인 주도 설계</strong>에 기반한 <strong>layered architecture</strong> 로 개발을 시작하게 되었습니다.</p>
                        <p>인터넷에 많은 자료가 있지는 않은 구조라 프로젝트 진행 초반에는 어려움이 있었지만만 진행하는 과정에서 구조에 대한 질문을 던지고 그 답변을 해나가는 과정에서도 많은 깨달음이 있었습니다.</p>
                        <p>그렇게 4단계 구조으 아키텍처가 완성되었고 특정 기술에 종속적이지 않은 코드를 만들 수 있게 되었습니다.</p>
                        <h2>사용된 기술</h2>
                        <p>사용된 기술은 크게 백엔드와 프론트엔드 그리고 배포로 나눠볼 수 있습니다.</p>
                        <p>백엔드 같은 경우는 아래의 기술을 사용했습니다.</p>
                        <ul>
                        <li>Spring</li>
                        <li>Mysql</li>
                        <li>Redis</li>
                        <li>RabbitMq</li>
                        </ul>
                        <p>프론트엔드는 아래의 기술을 사용했습니다.</p>
                        <ul>
                        <li>thymeleaf</li>
                        <li>htmx</li>
                        <li>bulma (css framework)</li>
                        </ul>
                        <p>저는 프론트엔드 개발자로써 경력이 있기에 react 에 익숙한 상황이었지만 react 에만 너무 몰두한 나머지 웹의 기본을 등한시해버렸습니다.</p>
                        <p>때문에 이번 프로젝트에서는 react 를 사용하지 않고 spring의 대표 template engine 인 thymeleaf 를 사용하게 되었습니다.</p>
                        <p>&nbsp;</p>
                        <p>&nbsp;</p>
                        <p>&nbsp;</p>
                        <p>&nbsp;</p>
                                                """).status(Post.Status.PUBLISHED).build();
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
