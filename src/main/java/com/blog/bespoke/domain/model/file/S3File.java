package com.blog.bespoke.domain.model.file;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 1. post cover
 * 2. post images
 * 3. user profile cover
 */
@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class S3File {
    protected String url;

    protected String originalFilename;

    protected String filename; // random generated. uploaded by this name

    protected Long size;

    protected String mimeType; // string 으로 저장
}
