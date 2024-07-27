create table s3_post_image
(
    s3_post_image_id bigint primary key auto_increment,
    post_id bigint,
    url varchar(255),
    original_filename varchar(255),
    filename varchar(50), # uuid (36) + ext (14)
    size bigint,
    mime_type varchar(255),
    type varchar(20) default 'CONTENT', # COVER, CONTENT(일반 이미지)
    foreign key (post_id) references post (post_id) on delete cascade
);

ALTER TABLE post
    ADD COLUMN cover_image_id BIGINT,
    ADD CONSTRAINT fk_cover_image FOREIGN KEY (cover_image_id) REFERENCES s3_post_image (s3_post_image_id) ON DELETE SET NULL;