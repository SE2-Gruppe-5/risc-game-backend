package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

public record UpdatePhaseMessage(int phase) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.UPDATE_PHASE;
    }
}
