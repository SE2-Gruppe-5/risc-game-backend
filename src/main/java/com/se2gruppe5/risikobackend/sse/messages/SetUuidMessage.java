package com.se2gruppe5.risikobackend.sse.messages;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.UUID;

public record SetUuidMessage(UUID uuid) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.SET_UUID;
    }
}
