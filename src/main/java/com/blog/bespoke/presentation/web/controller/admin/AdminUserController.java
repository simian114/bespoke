package com.blog.bespoke.presentation.web.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    @GetMapping
    public ResponseEntity<?> adminTest() {
        return ResponseEntity.ok("ok");
    }
}
