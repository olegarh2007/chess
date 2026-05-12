package com.olegator.chess.dto.chat;

import java.util.Set;

public record ChatProfileDto(Set<Long> usersIds, String name, String description) {
}
