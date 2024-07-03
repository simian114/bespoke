create table post
(
    post_id     bigint primary key auto_increment,

    title       varchar(100) not null,       # id 기반으로.. user 가 작성한 게시글에서만 unique하면 됨
    description varchar(200),
    content     longtext,

    status      varchar(20) default 'DRAFT', # DRAFT, PUBLISHED, BLOCKED

    author_id   bigint       not null,

    # tags varchar(255), # ,로 구분되는 문자열.

    # TODO: category_id
    foreign key (author_id) references users (user_id) on delete cascade,

    created_at  timestamp   default current_timestamp,
    updated_at  timestamp on update current_timestamp,
    deleted_at  timestamp
);

create table comment
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

create table post_like
(
    post_like_id bigint primary key auto_increment,
    post_id      bigint not null,
    user_id      bigint not null,
    foreign key (post_id) references post (post_id) on delete cascade,
    foreign key (user_id) references users (user_id) on delete cascade,
    created_at   timestamp default current_timestamp,
    constraint unique_post_like unique (post_id, user_id)
);


create table comment_like
(
    comment_like_id bigint primary key auto_increment,
    comment_id      bigint not null,
    user_id         bigint not null,
    foreign key (comment_id) references comment (comment_id) on delete cascade,
    foreign key (user_id) references users (user_id) on delete cascade,
    created_at      timestamp default current_timestamp
);

create table post_count_info
(
    post_count_info_id bigint primary key auto_increment,
    post_id bigint not null,
    like_count bigint not null default 0,
    view_count bigint not null default 0,
    comment_count bigint not null default 0,
    foreign key (post_id) references post (post_id)
)

