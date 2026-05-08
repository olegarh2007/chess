package com.olegator.chess.dto;

import java.time.LocalDateTime;

public record MessageDto(Long id, Long senderId, String content,
                         String senderLogin, String senderAvatarUrl,
                         LocalDateTime timestamp) {
}
