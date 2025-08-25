package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginResponseDTO;
import com.pm.authservice.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final JwtUtil jwtUtil;

    public TestController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> testLogin() {
        String token = jwtUtil.generateToken("admin@test.com", "ADMIN");
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}