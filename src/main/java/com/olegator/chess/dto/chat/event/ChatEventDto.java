package com.olegator.chess.dto.chat.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserMessageDto.class, name = "MESSAGE"),
        @JsonSubTypes.Type(value = AddUserEventDto.class, name = "ADD_USER")
})
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
public sealed abstract class ChatEventDto
        permits AddUserEventDto, UserMessageDto {
    private final Long id;
    private final LocalDateTime timestamp;
}
