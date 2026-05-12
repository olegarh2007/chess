package com.olegator.chess.entity.chat.event;

import com.olegator.chess.entity.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ADD_USER")
public class AddUserEvent extends ChatEvent {
    @ManyToOne
    @JoinColumn(name = "adder_id")
    private User adder;

    @ManyToOne
    @JoinColumn(name = "added_id")
    private User added;
}
