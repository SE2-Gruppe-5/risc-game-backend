package com.se2gruppe5.risikobackend.chat.messages;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

public record ChatMessage(String message) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.CHAT;
    }
}
