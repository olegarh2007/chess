package com.olegator.chess.unit;

import com.olegator.chess.controller.ChatResrController;
import com.olegator.chess.dto.ChatSummaryDto;
import com.olegator.chess.service.ChatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatResrController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatService chatService;

    @Test
    void getUserChats() throws Exception {
        Mockito.doReturn(Set.of(
                new ChatSummaryDto(1L, "Chat name 1", "https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-High-Quality-Image.png", "Chat 1", LocalDateTime.now()),
                new ChatSummaryDto(2L, "Chat name 2", "https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-High-Quality-Image.png", "Chat 2", LocalDateTime.now())
        )).when(chatService).getMyChats(Mockito.anyLong());
        final long id = 1L;
        mockMvc.perform(get("/api/chats/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                "id": 1,
                                "name": "Chat name 1",
                                "avatarUrl": "https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-High-Quality-Image.png",
                                "lastMessage": "Chat 1"
                            },
                            {
                                "id": 2,
                                "name": "Chat name 2",
                                "avatarUrl": "https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-High-Quality-Image.png",
                                "lastMessage": "Chat 2"
                            }
                        ]
                        """));
    }
}
