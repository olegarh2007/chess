package com.olegator.chess.controller;

import com.olegator.chess.dto.UserResponseDto;
import com.olegator.chess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{id}")
    private ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        var maybePage = userService.getUserPublicProfile(id);
        return maybePage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
