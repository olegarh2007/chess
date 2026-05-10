package com.olegator.chess.integration;

import com.olegator.chess.annotation.IT;
import com.olegator.chess.stompcontroller.ChatStompController;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@IT
@Transactional
class ChatStompControllerTest {
    @Autowired
    private ChatStompController chatStompController;

    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;
}