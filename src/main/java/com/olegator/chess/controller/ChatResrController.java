package com.olegator.chess.controller;

import com.olegator.chess.dto.ChatSummaryDto;
import com.olegator.chess.dto.MessageDto;
import com.olegator.chess.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ChatResrController {
    private final ChatService chatService;

    @GetMapping("/api/chats/{userId}")
    private ResponseEntity<Set<ChatSummaryDto>> getMyChats(@PathVariable Long userId) {
        ResponseEntity<Set<ChatSummaryDto>> responseEntity = ResponseEntity.ok(chatService.getMyChats(userId));
        return responseEntity;
    }

    @GetMapping("/api/chats/{id}/messages")
    private ResponseEntity<List<MessageDto>> getChatMessages(@PathVariable Long id) {
        List<MessageDto> messages = chatService.getChatMessages(id);
        return ResponseEntity.ok(messages);
    }
}
