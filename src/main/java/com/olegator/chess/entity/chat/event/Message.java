package com.olegator.chess.entity.chat.event;

import com.olegator.chess.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("MESSAGE")
public class Message extends ChatEvent {
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private String content;
}
