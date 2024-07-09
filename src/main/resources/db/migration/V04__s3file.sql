create table s3_post_image
(
    s3_post_image_id bigint primary key auto_increment,
    post_id bigint,
    url varchar(255),
    original_filename varchar(255),
    filename varchar(50), # uuid (36) + ext (14)
    size bigint,
    mime_type varchar(255),
    foreign key (post_id) references post (post_id) on delete cascade
);