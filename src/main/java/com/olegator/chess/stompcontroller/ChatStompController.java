package com.olegator.chess.stompcontroller;

import com.olegator.chess.dto.chat.event.UserMessageDto;
import com.olegator.chess.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.{chatId}")
    public void handleChatMessage(@DestinationVariable Long chatId,
                                  @Payload UserMessageDto chatMessageDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        messagingTemplate.convertAndSend("/topic/chat." + chatId, chatMessageDto);
    }
}
