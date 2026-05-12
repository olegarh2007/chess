package com.olegator.chess.dto.chat;

import java.util.Set;

public record ChatCreationDto(String name, String description,
                              Set<Long> memberIds) {
}
