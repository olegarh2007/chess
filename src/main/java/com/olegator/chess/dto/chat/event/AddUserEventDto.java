package com.olegator.chess.dto.chat.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public final class AddUserEventDto extends ChatEventDto {
    private final Long id;
    private final Long adderId;
    private final Long addedId;

    public AddUserEventDto(Long id, Long adderId, Long addedId,
                           LocalDateTime timestamp) {
        super(id, timestamp);
        this.id = id;
        this.adderId = adderId;
        this.addedId = addedId;
    }
}
