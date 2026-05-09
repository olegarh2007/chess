package com.olegator.chess.mapper;

import com.olegator.chess.dto.MessageDto;
import com.olegator.chess.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderLogin", source = "sender.email")
    @Mapping(target = "senderAvatarUrl", source = "sender.userMedia.avatarUrl")
    MessageDto toMessageDto(Message message);
}
