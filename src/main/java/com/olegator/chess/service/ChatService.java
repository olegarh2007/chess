package com.olegator.chess.service;

import com.olegator.chess.dto.chat.ChatCreatedDto;
import com.olegator.chess.dto.chat.ChatCreationDto;
import com.olegator.chess.dto.chat.ChatSummaryDto;
import com.olegator.chess.dto.chat.event.AddUserEventDto;
import com.olegator.chess.dto.chat.event.ChatEventDto;
import com.olegator.chess.entity.chat.Chat;
import com.olegator.chess.entity.chat.UserChat;
import com.olegator.chess.entity.chat.event.AddUserEvent;
import com.olegator.chess.entity.user.User;
import com.olegator.chess.mapper.ChatMapper;
import com.olegator.chess.mapper.ChatEventMapper;
import com.olegator.chess.repository.ChatRepository;
import com.olegator.chess.repository.UserChatRepository;
import com.olegator.chess.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final ChatEventMapper chatEventMapper;
    private final UserChatRepository userChatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private void addUsers(Long adderId, Set<Long> usersIds, Chat chat) {
        User adder = userRepository.findById(adderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid adder id " + adderId));
        usersIds.forEach(userId -> {
            var maybeUser = userRepository.findById(userId);
            if (maybeUser.isEmpty()) {
                throw new IllegalArgumentException("User with id " + userId + " not found");
            }
            User added = maybeUser.get();
            chat.addUser(added);
            var addEvent = new AddUserEvent();
            addEvent.setAdder(adder);
            addEvent.setAdded(added);
            chat.addEvent(addEvent);
            sendToChat(chat.getId(),
                    new AddUserEventDto(addEvent.getId(),
                            adderId, added.getId(), LocalDateTime.now())
            );
        });
    }

    public void addUsersToChat(Long chatId, Long adderId, Set<Long> users_ids) {
        var maybeChat = chatRepository.findById(chatId);
        if (maybeChat.isEmpty()) {
            throw new IllegalArgumentException("Chat with id " + chatId + " not found");
        }
        if (!userChatRepository.existsByChatIdAndUserId(chatId, adderId)) {
            throw new IllegalArgumentException("User " + adderId + " doesnt have access to chat " + chatId);
        }
        Chat chat = maybeChat.get();
        addUsers(adderId, users_ids, chat);
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

    public List<ChatEventDto> getChatMessages(Long chatId, Long userId) {
        var maybeChat = chatRepository.findById(chatId);
        if (maybeChat.isEmpty()) {
            throw new IllegalArgumentException("Chat with id " + chatId + " not found");
        }
        Chat chat = maybeChat.get();
        if (userChatRepository.existsByChatIdAndUserId(chatId, userId)) {
            throw new AccessDeniedException("Access to chat denied: user isn't in chat");
        }
        List<ChatEventDto> events = chat.getEvents().stream()
                .map(chatEventMapper::mapChatEvent)
                .collect(Collectors.toList());
        return events;
    }

    public ChatCreatedDto createNewChat(ChatCreationDto chatCreationDto, Long creatorId) {
        Chat chat = new Chat();
        chat.setName(chatCreationDto.name());
        chat.setDescription(chatCreationDto.description());
        var user = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalStateException("No user with id " + creatorId));
        chat.addUser(user);
        addUsers(creatorId, chatCreationDto.memberIds(), chat);
        chatRepository.save(chat);
        return chatMapper.mapToCreated(chat);
    }

    private void sendToChat(Long chatId, ChatEventDto addMessage) {
        messagingTemplate.convertAndSend("/app/chat." + chatId, addMessage);
    }
}
