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


insert into users(user_id, email, password, nickname, name, status)
values (2, 'user2@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user2', 'user2',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (2, 0, 0, 0, 0);


insert into users(user_id, email, password, nickname, name, status)
values (3, 'user3@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user3', 'user3',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (3, 0, 0, 0, 0);


insert into users(user_id, email, password, nickname, name, status)
values (4, 'user4@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user4', 'user4',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (4, 0, 0, 0, 0);


insert into users(user_id, email, password, nickname, name, status)
values (5, 'user5@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user5', 'user5',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (5, 0, 0, 0, 0);


insert into users(user_id, email, password, nickname, name, status)
values (6, 'user6@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user6', 'user6',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (6, 0, 0, 0, 0);


insert into users(user_id, email, password, nickname, name, status)
values (7, 'user7@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user7', 'user7',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (7, 0, 0, 0, 0);


insert into users(user_id, email, password, nickname, name, status)
values (8, 'user8@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user8', 'user8',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (8, 0, 0, 0, 0);


insert into users(user_id, email, password, nickname, name, status)
values (9, 'user9@gmail.com', '$2a$10$upVxhxlPCfPTg9rUUTFZmeK1h2E6pvx8n.OIGFeMKHgB57X1JltdK', 'user9', 'user9',
        'ACTIVE');

insert into user_count_info(user_id, follower_count, following_count, published_post_count, like_post_count)
values (9, 0, 0, 0, 0);

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
