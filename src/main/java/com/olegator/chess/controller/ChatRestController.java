package com.olegator.chess.controller;

import com.olegator.chess.dto.chat.ChatCreatedDto;
import com.olegator.chess.dto.chat.ChatCreationDto;
import com.olegator.chess.dto.chat.ChatSummaryDto;
import com.olegator.chess.dto.AddMemberDto;
import com.olegator.chess.dto.chat.event.ChatEventDto;
import com.olegator.chess.security.UserDetailsImpl;
import com.olegator.chess.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatService chatService;

    @GetMapping("/api/chats")
    public ResponseEntity<Set<ChatSummaryDto>> getMyChats(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<Set<ChatSummaryDto>> responseEntity = ResponseEntity.ok(chatService.getMyChats(userDetails.getId()));
        return responseEntity;
    }

    @GetMapping("/api/chats/{chatId}/messages")
    public ResponseEntity<List<ChatEventDto>> getChatMessages(@PathVariable Long chatId,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatEventDto> messages = chatService.getChatMessages(chatId, userDetails.getId());
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/api/chats/create")
    public ResponseEntity<ChatCreatedDto> createNewChat(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody ChatCreationDto chatCreationDto) {
        var creationResponse = chatService.createNewChat(chatCreationDto, userDetails.getId());
        return ResponseEntity.created(URI.create("/api/chats." + creationResponse.id())).body(creationResponse);
    }

    @PostMapping("/api/chats/{chatId}/members")
    public ResponseEntity<?> addToChat(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody AddMemberDto addMemberDto,
                                             @PathVariable Long chatId) {
        chatService.addUsersToChat(chatId, userDetails.getId(), addMemberDto.userIds());

        return ResponseEntity.ok().build();
    }
}
