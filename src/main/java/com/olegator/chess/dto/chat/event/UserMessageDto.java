package com.olegator.chess.dto.chat.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class UserMessageDto extends ChatEventDto {
    private final Long senderId;
    private final String content;
    private final String senderLogin;
    private final String senderAvatarUrl;

    public UserMessageDto(Long id, Long senderId, String content,
                          String senderLogin, String senderAvatarUrl,
                          LocalDateTime timestamp) {
        super(id, timestamp);
        this.senderId = senderId;
        this.content = content;
        this.senderLogin = senderLogin;
        this.senderAvatarUrl = senderAvatarUrl;
    }
}
