# role
INSERT INTO role(role_id, code)
values (1, 'USER');

INSERT INTO role(role_id, code)
values (2, 'ADMIN');

# user
insert into users(user_id, email, password, nickname, name, status)
values (10000, 'admin@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'admin', 'admin',
        'ACTIVE');



insert into users(user_id, email, password, nickname, name, status)
values (1, 'user@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user', 'user', 'ACTIVE');


insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (1, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (1, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (2, 'user2@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user2', 'user2',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (2, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (2, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (3, 'user3@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user3', 'user3',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (3, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (3, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (4, 'user4@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user4', 'user4',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (4, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (4, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (5, 'user5@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user5', 'user5',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (5, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (5, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (6, 'user6@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user6', 'user6',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (6, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (6, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (7, 'user7@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user7', 'user7',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (7, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (7, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (8, 'user8@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user8', 'user8',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (8, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (8, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into users(user_id, email, password, nickname, name, status)
values (9, 'user9@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user9', 'user9',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (9, 0, 0, 0, 0);

insert into user_profile(user_id, introduce)
values (9, '<p>안녕하세요 남상혁입니다.</p><p>지난 2년간 스터닝에서 프론트엔드 개발자로 근무했습니다.</p><p><a href="https://www.loud.kr/" target="_blank" rel="noopener">라우드소싱</a>과<a href="https://notefolio.net/"> 노트폴리오</a> 개발 및 유지/보수 를 담당했습니다.</p><p>&nbsp;</p><p>더 좋은 환경을 만들어 나가기 위해 노력합니다.</p><p>그 일환으로 개발팀에서 사용하는 <a href="https://marketplace.visualstudio.com/items?itemName=namSSang.code-text-lens" target="_blank" rel="noopener">vscode 플러그인</a>을 개발했고 다양한 공통 컴포넌트와 개발 규칙을 만들어 냈습니다.</p><p>그리고 회사 내 많은 사람들이 사용할 수 있는 pc 프로그램도 개발했습니다.</p><p>&nbsp;</p><p>지금은 퇴사 후 더 좋은 개발자가 되기 위해 백엔드를 공부하고 있습니다.</p><p>보고계신 이 블로그는 <strong>spring</strong>&nbsp;을 사용해 개발하고 있습니다.&nbsp; <a href="https://github.com/simian114/bespoke" target="_blank" rel="noopener">github</a> 에서 확인하실 수 있습니다.</p><p>저에 대한 더 자세한 내용은 <a href="https://bespoke-blog.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%A1+%E1%84%82%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A1%E1%86%BC%E1%84%92%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%B5%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.pdf" target="_blank" rel="noopener">이력서</a>에서 확인 하실 수 있습니다.</p><p>감사합니다.</p>');

insert into user_role(user_id, role_id)
values (1, 1);

insert into user_role(user_id, role_id)
values (2, 2);

insert into user_role(user_id, role_id)
values (3, 2);
insert into user_role(user_id, role_id)
values (4, 2);
insert into user_role(user_id, role_id)
values (5, 2);
insert into user_role(user_id, role_id)
values (6, 2);
insert into user_role(user_id, role_id)
values (7, 2);
insert into user_role(user_id, role_id)
values (8, 2);
insert into user_role(user_id, role_id)
values (9, 2);
