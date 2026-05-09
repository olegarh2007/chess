package com.olegator.chess.controller;

import com.olegator.chess.dto.UserRegDto;
import com.olegator.chess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ResponseStatus
public class RegistrationController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegDto userRegDto) {
        userService.registerUser(userRegDto);
        return ResponseEntity.ok("User registered successfully");
    }
}
