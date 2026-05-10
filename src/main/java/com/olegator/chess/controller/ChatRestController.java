package com.olegator.chess.controller;

import com.olegator.chess.dto.ChatSummaryDto;
import com.olegator.chess.dto.MessageDto;
import com.olegator.chess.security.UserDetailsImpl;
import com.olegator.chess.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatService chatService;

    @GetMapping("/api/chats")
    private ResponseEntity<Set<ChatSummaryDto>> getMyChats(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<Set<ChatSummaryDto>> responseEntity = ResponseEntity.ok(chatService.getMyChats(userDetails.getId()));
        return responseEntity;
    }

    @GetMapping("/api/chats/{chatId}/messages")
    private ResponseEntity<List<MessageDto>> getChatMessages(@PathVariable Long chatId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MessageDto> messages = chatService.getChatMessages(chatId, userDetails.getId());
        return ResponseEntity.ok(messages);
    }
}
