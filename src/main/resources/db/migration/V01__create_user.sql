create table users
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
);

create table role (
    role_id bigint primary key auto_increment,
    code varchar(10) not null unique
);

create table user_role (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users(user_id) on delete cascade,
    foreign key (role_id) references role(role_id) on delete cascade
);

INSERT INTO role(code) values ('USER');
INSERT INTO role(code) values ('ADMIN');
