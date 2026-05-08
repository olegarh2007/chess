package com.olegator.chess.dto;

import java.util.Set;

public record ChatProfileDto(Set<Long> usersIds, String name, String description) {
}
