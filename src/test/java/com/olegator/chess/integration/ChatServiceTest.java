package com.olegator.chess.integration;

import com.olegator.chess.annotation.IT;
import com.olegator.chess.entity.Chat;
import com.olegator.chess.entity.User;
import com.olegator.chess.repository.ChatRepository;
import com.olegator.chess.repository.UserRepository;
import com.olegator.chess.service.ChatService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void addUsersToChat_shouldAddUsersToExistingChat() {
        User firstUser = userRepository.save(createUser("user-1", "user-1@mail.com"));
        User secondUser = userRepository.save(createUser("user-2", "user-2@mail.com"));

        Chat chat = chatRepository.save(createChat());

        chatService.addUsersToChat(chat.getId(), Set.of(firstUser.getId(), secondUser.getId()));

        Chat savedChat = chatRepository.findById(chat.getId()).orElseThrow();
        assertEquals(2, savedChat.getUserChats().size());

        assertTrue(savedChat.getUserChats().stream().anyMatch(userChat -> userChat.getUser().getId().equals(firstUser.getId())));
        assertTrue(savedChat.getUserChats().stream().anyMatch(userChat -> userChat.getUser().getId().equals(secondUser.getId())));
    }

    @Test
    void addUsersToChat_shouldThrowWhenChatDoesNotExist() {
        User user = userRepository.save(createUser("user-1", "user-1@mail.com"));

        assertThrows(IllegalArgumentException.class,
                () -> chatService.addUsersToChat(999L, Set.of(user.getId())));
    }

    @Test
    void addUsersToChat_shouldThrowWhenUserDoesNotExist() {
        Chat chat = chatRepository.save(createChat());

        assertThrows(IllegalArgumentException.class,
                () -> chatService.addUsersToChat(chat.getId(), Set.of(999L)));
    }

    private User createUser(String login, String email) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setPasswordHash("password");
        return user;
    }

    private Chat createChat() {
        Chat chat = new Chat();
        chat.setName("general");
        chat.setDescription("General chat");
        return chat;
    }
}
