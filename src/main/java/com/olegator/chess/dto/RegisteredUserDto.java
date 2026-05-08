package com.olegator.chess.dto;

import java.util.Set;

public record RegisteredUserDto(String login, String avatarUrl,
                                String email) {
}
