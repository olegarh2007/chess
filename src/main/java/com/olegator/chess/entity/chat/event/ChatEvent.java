package com.olegator.chess.entity.chat.event;

import com.olegator.chess.entity.chat.Chat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "chat_event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ChatEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
