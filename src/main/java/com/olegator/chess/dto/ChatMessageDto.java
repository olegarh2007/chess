package com.olegator.chess.dto;

public record ChatMessageDto(
        Long senderId,
        String content,
        Long timestamp) {
}
