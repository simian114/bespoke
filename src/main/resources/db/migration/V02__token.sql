create table token
(
    token_id   bigint primary key auto_increment,
    type       varchar(30) not null, # EMAIL, REFRESH_TOKEN
    ref_id     bigint      not null,
    ref_type   varchar(30) not null, # USER, POST
    expired_at timestamp   not null,
    code       varchar(36) not null,
    index idx_code (code)            # code 에 index 추가
);
