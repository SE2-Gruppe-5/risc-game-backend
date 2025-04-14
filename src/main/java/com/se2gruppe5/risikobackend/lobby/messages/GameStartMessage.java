package com.se2gruppe5.risikobackend.lobby.messages;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.UUID;

public record GameStartMessage(UUID gameId) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.START_GAME;
    }
}
