package com.olegator.chess.dto;

import java.util.Set;

public record AddMemberDto(Set<Long> userIds) {
}
