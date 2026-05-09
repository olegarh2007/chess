package com.olegator.chess.controller;

import com.olegator.chess.dto.LoginDto;
import com.olegator.chess.service.JwtService;
import com.olegator.chess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        userService.checkUserLogin(loginDto.email(), loginDto.password());
        String token = jwtService.generateToken(loginDto.email());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
