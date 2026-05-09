package com.olegator.chess.controller;

import com.olegator.chess.dto.LoginDto;
import com.olegator.chess.service.JwtService;
import com.olegator.chess.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        String username = "testUser";
        String password = "testPassword";
        String token = "mockToken";

        when(jwtService.generateToken(username)).thenReturn(token);

        ResponseEntity<Map<String, String>> response = loginController.login(new LoginDto(username, password));

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Map.of("token", token), response.getBody());
    }

    @Test
    void testLoginFailure() {
        String username = "testUser";
        String password = "wrongPassword";

        ResponseEntity<Map<String, String>> response = loginController.login(new LoginDto(username, password));

        assertEquals(401, response.getStatusCode().value());
        assertEquals(Map.of("error", "Invalid credentials"), response.getBody());
    }
}
