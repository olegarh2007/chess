package com.olegator.chess.service;

import com.olegator.chess.entity.user.User;
import com.olegator.chess.repository.UserChatRepository;
import com.olegator.chess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserChatService {
    private final UserChatRepository userChatRepository;
    private final UserRepository userRepository;

    public boolean isMemberByEmail(String email, Long chatId) {
        Long userId = userRepository.findByEmail(email).map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("illegal email"));
        return userChatRepository.existsByChatIdAndUserId(chatId, userId);
    }
}
