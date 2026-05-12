package com.olegator.chess.controller;

import com.olegator.chess.dto.UserRegDto;
import com.olegator.chess.service.JwtService;
import com.olegator.chess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@ResponseStatus
public class RegistrationController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserRegDto userRegDto) {
        userService.registerUser(userRegDto);
        String token = jwtService.generateToken(userRegDto.email());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
