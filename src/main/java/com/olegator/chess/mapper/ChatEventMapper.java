package com.olegator.chess.mapper;

import com.olegator.chess.dto.chat.event.AddUserEventDto;
import com.olegator.chess.dto.chat.event.ChatEventDto;
import com.olegator.chess.dto.chat.event.UserMessageDto;
import com.olegator.chess.entity.chat.event.AddUserEvent;
import com.olegator.chess.entity.chat.event.ChatEvent;
import com.olegator.chess.entity.chat.event.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatEventMapper {
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderLogin", source = "sender.login")
    @Mapping(target = "senderAvatarUrl", source = "sender.userMedia.avatarUrl")
    UserMessageDto mapUserMessage(Message message);

    @Mapping(target = "adderId", source = "adder.id")
    @Mapping(target = "addedId", source = "added.id")
    AddUserEventDto mapAddUserEvent(AddUserEvent addUserEvent);

    default ChatEventDto mapChatEvent(ChatEvent chatEvent) {
        if (chatEvent instanceof Message message) {
            return mapUserMessage(message);
        }
        if (chatEvent instanceof AddUserEvent addUserEvent) {
            return mapAddUserEvent(addUserEvent);
        }
        throw new IllegalArgumentException("Unknown chat event type: " + chatEvent.getClass());
    }
}
