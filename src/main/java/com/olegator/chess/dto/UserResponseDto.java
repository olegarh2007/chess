package com.olegator.chess.dto;

import java.time.LocalDate;

public record UserResponseDto(String login, String avatarUrl,
                              LocalDate registrationDate) {
}
