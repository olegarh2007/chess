package com.olegator.chess.controller;

import com.olegator.chess.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.{chatId}")
    public void handleChatMessage(@DestinationVariable Long chatId,
                                  @Payload ChatMessageDto chatMessageDto,
                                  Principal principal) {
        messagingTemplate.convertAndSend("/topic/chat." + chatId, chatMessageDto);
        
    }
}
