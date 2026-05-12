package com.olegator.chess.entity.chat;

import com.olegator.chess.entity.chat.event.ChatEvent;
import com.olegator.chess.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chat")
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatMedia chatMedia;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatEvent> events = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserChat> userChats = new HashSet<>();

    public void addUser(User user) {
        UserChat userChat = new UserChat();
        userChat.setUser(user);
        userChat.setChat(this);
        userChats.add(userChat);
        user.addChat(userChat);
    }

    public void addEvent(ChatEvent event) {
        events.add(event);
        event.setChat(this);
    }
}
