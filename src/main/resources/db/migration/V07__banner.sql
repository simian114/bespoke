create table if not exists banner
(
    banner_id        bigint primary key auto_increment,
    name             varchar(255) not null,
    title            varchar(25)  not null,
    sub_title        varchar(40),
    content          varchar(255),
    link             varchar(255),
    ui_type          varchar(25)  not null, # MAIN_HERO, ...
    background_color varchar(10),
    created_at       timestamp default current_timestamp,
    updated_at       timestamp on update current_timestamp,
    deleted_at       timestamp,             # 삭제 여부
    user_id          bigint       not null,
    foreign key (user_id) references users (user_id) on delete cascade
);

create table if not exists s3_banner_image
(
    s3_banner_image_id bigint primary key auto_increment,
    url                varchar(255),
    original_filename  varchar(255),
    filename           varchar(50),
    size               bigint,
    mime_type          varchar(255),
    type               varchar(20) default 'PC', # PC / MOBILE
    created_at         timestamp   default current_timestamp,
    updated_at         timestamp on update current_timestamp,
    deleted_at         timestamp,                # 삭제 여부

    banner_id          bigint not null,
    foreign key (banner_id) references banner (banner_id) on delete cascade
);

create table if not exists banner_form
(
    banner_form_id  bigint primary key auto_increment,
    start_date      timestamp   not null,
    end_date        timestamp   not null,
    banner_snapshot longtext    not null,                   # 요청한 그 순간의 banner_type. image 정보들 또한 같이 들어가야한다.
    status          varchar(30) not null default 'PENDING', # PENDING, APPROVED, DENIED
    result          varchar(255),                           # 심사 결과. DENIED 면 거절 이유. APPROVED 면 '승인되었습니다.'
    banner_id       bigint      not null,
    user_id         bigint      not null,
    ui_type         varchar(25) not null,                   # MAIN_HERO, ...
    audited_at      timestamp,
    created_at      timestamp            default current_timestamp,
    updated_at      timestamp on update current_timestamp,
    deleted_at      timestamp,                              # 삭제 여부

    foreign key (banner_id) references banner (banner_id) on delete cascade,
    foreign key (user_id) references users (user_id)
);

# create table if not exists payment
# (
#     payment_id bigint primary key auto_increment
# );
