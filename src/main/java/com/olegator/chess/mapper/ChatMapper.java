package com.olegator.chess.mapper;

import com.olegator.chess.dto.chat.ChatCreatedDto;
import com.olegator.chess.dto.chat.ChatMediaDto;
import com.olegator.chess.dto.chat.ChatProfileDto;
import com.olegator.chess.dto.chat.ChatSummaryDto;
import com.olegator.chess.entity.chat.Chat;
import com.olegator.chess.entity.chat.ChatMedia;
import com.olegator.chess.entity.chat.event.ChatEvent;
import com.olegator.chess.entity.chat.event.Message;
import com.olegator.chess.entity.chat.UserChat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(target = "usersIds", source = "userChats")
    ChatProfileDto mapToProfile(Chat chat);
    @Mapping(target = "avatarUrl", source = "chatMedia.avatarUrl")
    @Mapping(target = "lastMessage", source = "events")
    @Mapping(target = "lastMessageTime", source = "events")
    ChatSummaryDto mapToSummary(Chat chat);

    ChatCreatedDto mapToCreated(Chat chat);
    ChatMediaDto mapChatMedia(ChatMedia chatMedia);

    default Long getUserId(UserChat userChat) {
        return userChat.getUser().getId();
    }

    default String getLastMessageContent(List<ChatEvent> chatEvents) {
        if (chatEvents.isEmpty()) {
            return "";
        }
        return chatEvents.getLast().toString(); //TODO make content getter
    }

    default LocalDateTime getLastMessageTime(List<ChatEvent> chatEvents) {
        if (chatEvents.isEmpty()) {
            return null;
        }
        return chatEvents.getLast().getTimestamp();
    }
}
