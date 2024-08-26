create table if not exists payment
(
    payment_id   bigint primary key auto_increment,
    ref_id       bigint       not null,
    ref_type     varchar(30)  not null,
    amount       bigint       not null default 0,

    # toss payment info start
    payment_key  varchar(255),
    type         varchar(36),
    order_id     varchar(36)  not null unique, # uuid
    order_name   varchar(255) not null,
    m_id         varchar(14),
    method       varchar(30),
    requested_at timestamp,
    approved_at  timestamp,
    vat          bigint,
    status       varchar(36),
    # toss payment info end

    created_at   timestamp             default current_timestamp,
    updated_at   timestamp on update current_timestamp,
    deleted_at   timestamp,                    # 삭제 여부

    user_id      bigint       not null,
    foreign key (user_id) references users (user_id) on delete cascade
);

# CREATE INDEX idx_ref_id_ref_type ON payment (ref_id, ref_type);
