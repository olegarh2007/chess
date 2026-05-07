package com.olegator.chess.dto;

import java.util.Set;

public record ChatResponseDto(Set<Long> usersIds, String name, String description) {
}
