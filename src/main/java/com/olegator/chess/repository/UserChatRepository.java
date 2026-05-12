package com.olegator.chess.repository;

import com.olegator.chess.entity.chat.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    boolean existsByChatIdAndUserId(Long chatId, Long userId);
}
