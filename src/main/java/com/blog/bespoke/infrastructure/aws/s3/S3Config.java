package com.blog.bespoke.infrastructure.aws.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${aws.s3.access-key}")
    public String s3AccessKey;

    @Value("${aws.s3.secret-key}")
    public String s3SecretKey;

    @Bean
    public AmazonS3 getAmazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

}
