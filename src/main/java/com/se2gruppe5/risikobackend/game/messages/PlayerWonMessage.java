package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.UUID;

public record PlayerWonMessage(UUID winner) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.PLAYER_WON;
    }
}
