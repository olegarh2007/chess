package com.olegator.chess.service;

import com.olegator.chess.dto.ChatSummaryDto;
import com.olegator.chess.dto.MessageDto;
import com.olegator.chess.entity.Chat;
import com.olegator.chess.entity.User;
import com.olegator.chess.entity.UserChat;
import com.olegator.chess.mapper.ChatMapper;
import com.olegator.chess.mapper.MessageMapper;
import com.olegator.chess.repository.ChatRepository;
import com.olegator.chess.repository.UserChatRepository;
import com.olegator.chess.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;
    private final UserChatRepository userChatRepository;

    public void createChat(Set<Long> users_ids) {
        Chat chat = new Chat();
        addUsers(users_ids, chat);
        chatRepository.save(chat);
    }

    public boolean isMemberByEmail(String email, Long chatId) {
        return userRepository.findByEmail(email).map(user ->
                user.getUserChats().stream()
                        .anyMatch(userChat -> userChat.getChat().getId().equals(chatId)))
                        .orElse(false);
    }

    private void addUsers(Set<Long> users_ids, Chat chat) {
        users_ids.forEach(userId -> {
            var maybeUser = userRepository.findById(userId);
            if (maybeUser.isEmpty()) {
                throw new IllegalArgumentException("User with id " + userId + " not found");
            }
            User user = maybeUser.get();
            chat.addUser(user);
        });
        chatRepository.save(chat);
    }

    public void addUsersToChat(Long chatId, Set<Long> users_ids) {
        var maybeChat = chatRepository.findById(chatId);
        if (maybeChat.isEmpty()) {
            throw new IllegalArgumentException("Chat with id " + chatId + " not found");
        }
        Chat chat = maybeChat.get();
        addUsers(users_ids, chat);
    }

    public Set<ChatSummaryDto> getMyChats(Long userId) {
        var maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
        User user = maybeUser.get();
        Set<ChatSummaryDto> chatSummaries = user.getUserChats().stream()
                .map(UserChat::getChat).map(chatMapper::mapToSummary)
                .collect(Collectors.toSet());
        return chatSummaries;
    }

    public List<MessageDto> getChatMessages(Long chatId, Long userId) {
        var maybeChat = chatRepository.findById(chatId);
        if (maybeChat.isEmpty()) {
            throw new IllegalArgumentException("Chat with id " + chatId + " not found");
        }
        Chat chat = maybeChat.get();
        if (userChatRepository.existsByChatIdAndUserId(chatId, userId)) {
            throw new AccessDeniedException("Access to chat denied: user isn't in chat");
        }
        List<MessageDto> messages = chat.getMessages().stream()
                .map(messageMapper::toMessageDto)
                .collect(Collectors.toList());
        return messages;
    }
}
