create table if not exists users
(
    user_id      bigint primary key auto_increment,
    email        varchar(50)  not null unique,
    password     varchar(100) not null,
    nickname     varchar(50)  not null unique,
    name         varchar(30),
    status       varchar(20),
    banned_until timestamp,
    created_at   timestamp default current_timestamp,
    updated_at   timestamp on update current_timestamp,
    deleted_at   timestamp
);

create table if not exists user_profile
(
    user_id   bigint primary key not null,
    introduce text,
    foreign key (user_id) references users (user_id) on delete cascade
);

create table if not exists role
(
    role_id bigint primary key auto_increment,
    code    varchar(10) not null unique
);

create table if not exists user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (user_id) on delete cascade,
    foreign key (role_id) references role (role_id) on delete cascade
);

create table if not exists follow
(
    follow_id    bigint primary key auto_increment,
    following_id bigint not null,
    follower_id  bigint not null,
    created_at   timestamp default current_timestamp,
    foreign key (following_id) references users (user_id) on delete cascade,
    foreign key (follower_id) references users (user_id) on delete cascade,
    constraint unique_follow unique (following_id, follower_id)
);

create table if not exists user_count_info
(
    user_id              bigint primary key not null,
    follower_count       bigint             not null default 0,
    following_count      bigint             not null default 0,
    published_post_count bigint             not null default 0,
    like_post_count      bigint             not null default 0,
    foreign key (user_id) references users (user_id) on delete cascade
);


create table if not exists token
(
    token_id   bigint primary key auto_increment,
    type       varchar(30) not null,
    ref_id     bigint      not null,
    ref_type   varchar(30) not null,
    expired_at timestamp   not null,
    code       varchar(36) not null,
    index idx_code (code)
);

create table if not exists category
(
    category_id bigint primary key auto_increment,
    name        varchar(10) not null,
    description varchar(50) not null,
    url         varchar(20) not null,
    visible     tinyint(1)  not null default 0,
    priority    int         not null default 0,
    created_at  timestamp            default current_timestamp,
    user_id     bigint      not null,
    foreign key (user_id) references users (user_id) on delete cascade,
    index idx_category_user_url (user_id, url)
);

create table if not exists post
(
    post_id        bigint primary key auto_increment,

    title          varchar(100) not null,
    description    varchar(200),
    content        longtext,

    status         varchar(20) default 'DRAFT',
    author_id      bigint       not null,

    cover_image_id bigint,

    category_id    bigint       null,

    foreign key (author_id) references users (user_id) on delete cascade,
    foreign key (category_id) references category (category_id) on delete set null,

    created_at     timestamp   default current_timestamp,
    updated_at     timestamp on update current_timestamp,
    deleted_at     timestamp
);

create table if not exists comment
(
    comment_id bigint primary key auto_increment,
    content    text,
    post_id    bigint not null,
    user_id    bigint not null,

    foreign key (post_id) references post (post_id) on delete cascade,
    foreign key (user_id) references users (user_id) on delete cascade,

    created_at timestamp default current_timestamp,
    updated_at timestamp on update current_timestamp,
    deleted_at timestamp
);

create table if not exists post_like
(
    post_like_id bigint primary key auto_increment,
    post_id      bigint not null,
    user_id      bigint not null,
    foreign key (post_id) references post (post_id) on delete cascade,
    foreign key (user_id) references users (user_id) on delete cascade,
    created_at   timestamp default current_timestamp,
    constraint unique_post_like unique (post_id, user_id)
);


create table if not exists comment_like
(
    comment_like_id bigint primary key auto_increment,
    comment_id      bigint not null,
    user_id         bigint not null,
    foreign key (comment_id) references comment (comment_id) on delete cascade,
    foreign key (user_id) references users (user_id) on delete cascade,
    created_at      timestamp default current_timestamp
);

create table if not exists post_count_info
(
    post_count_info_id bigint primary key auto_increment,
    post_id            bigint not null,
    like_count         bigint not null default 0,
    view_count         bigint not null default 0,
    comment_count      bigint not null default 0,
    foreign key (post_id) references post (post_id)
);

create table if not exists s3_post_image
(
    s3_post_image_id  bigint primary key auto_increment,
    post_id           bigint,
    url               varchar(255),
    original_filename varchar(255),
    filename          varchar(50),
    size              bigint,
    mime_type         varchar(255),
    foreign key (post_id) references post (post_id) on delete cascade
);

create table if not exists notification
(
    notification_id bigint primary key auto_increment,
    recipient_id    bigint      not null,
    publisher_id    bigint,
    created_at      datetime    not null default now(),
    ref_id          bigint,
    type            varchar(20) not null,
    readed          bool        not null default false,
    extra           json        not null,
    foreign key (recipient_id) references users (user_id) on delete cascade,
    foreign key (publisher_id) references users (user_id)
);
