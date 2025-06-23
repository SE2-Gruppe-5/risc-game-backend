package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.List;

public record ChangeTerritoryMessage(List<Territory> territories) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.UPDATE_TERRITORIES;
    }
}
