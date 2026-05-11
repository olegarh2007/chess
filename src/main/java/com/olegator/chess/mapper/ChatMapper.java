package com.olegator.chess.mapper;

import com.olegator.chess.dto.ChatCreatedDto;
import com.olegator.chess.dto.ChatMediaDto;
import com.olegator.chess.dto.ChatProfileDto;
import com.olegator.chess.dto.ChatSummaryDto;
import com.olegator.chess.entity.Chat;
import com.olegator.chess.entity.ChatMedia;
import com.olegator.chess.entity.Message;
import com.olegator.chess.entity.UserChat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(target = "usersIds", source = "userChats")
    ChatProfileDto mapToProfile(Chat chat);
    @Mapping(target = "avatarUrl", source = "chatMedia.avatarUrl")
    @Mapping(target = "lastMessage", source = "messages")
    @Mapping(target = "lastMessageTime", source = "messages")
    ChatSummaryDto mapToSummary(Chat chat);

    ChatCreatedDto mapToCreated(Chat chat);
    ChatMediaDto mapChatMedia(ChatMedia chatMedia);

    default Long getUserId(UserChat userChat) {
        return userChat.getUser().getId();
    }

    default String getLastMessageContent(List<Message> messages) {
        if (messages.isEmpty()) {
            return "";
        }
        return messages.getLast().getContent();
    }

    default LocalDateTime getLastMessageTime(List<Message> messages) {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.getLast().getTimestamp();
    }
}
