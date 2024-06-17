package com.blog.bespoke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BespokeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BespokeApplication.class, args);
    }
}
