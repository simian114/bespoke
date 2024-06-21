package com.blog.bespoke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class BespokeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BespokeApplication.class, args);
    }
}
