package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.UUID;

public record CheatAccusationMessage(UUID accusedPlayerUUID) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.ACCUSE_CHEATING;
    }
}
