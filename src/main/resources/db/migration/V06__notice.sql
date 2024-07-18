create table if not exists notification
(
	notification_id bigint primary key auto_increment,
	recipient_id bigint not null,
	publisher_id bigint,
	created_at datetime not null default now(),
	ref_id bigint,
	type varchar(20) not null,
    readed bool not null default false,
    extra json not null,
	foreign key (recipient_id) references users (user_id) on delete cascade,
	foreign key (publisher_id) references users (user_id)
);