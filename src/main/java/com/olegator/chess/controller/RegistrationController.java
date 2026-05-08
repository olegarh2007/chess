package com.olegator.chess.controller;

import com.olegator.chess.dto.UserRegDto;
import com.olegator.chess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ResponseStatus
public class RegistrationController {
    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestParam UserRegDto userRegDto) {
        try {
            userService.registerUser(userRegDto);
        } catch (Exception e) {
            return "Registration failed: " + e.getMessage();
        }
        userService.registerUser(userRegDto);
    }
}
