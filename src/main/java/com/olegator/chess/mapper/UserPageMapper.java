package com.olegator.chess.mapper;

import com.olegator.chess.dto.UserResponseDto;
import com.olegator.chess.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPageMapper {
    @Mapping(target = "avatarUrl", source = "user.userMedia.avatarUrl")
    @Mapping(target = "registrationDate", source = "user.userTimestamp.registrationDate")
    UserResponseDto map(User user);
}
