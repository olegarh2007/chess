package com.olegator.chess.dto;

import java.time.LocalDateTime;

public record ChatSummaryDto(Long id, String name, String avatarUrl,
                             String lastMessage, LocalDateTime lastMessageTime) {
}
