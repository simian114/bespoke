create table user
(
    user_id bigint primary key auto_increment,
    email varchar(50) not null unique,
    password varchar(100) not null,
    nickname varchar(50) not null unique,
    name varchar(30),
    role varchar(30) default 'user', # user, admin
    created_at timestamp default current_timestamp,
    updated_at timestamp on update current_timestamp,
    deleted_at timestamp
)