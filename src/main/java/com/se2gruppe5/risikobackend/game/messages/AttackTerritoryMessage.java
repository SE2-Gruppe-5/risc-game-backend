package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.UUID;

public record AttackTerritoryMessage(Territory from, Territory target, int troops) implements Message {

    @Override
    public MessageType getType() {
        return MessageType.ATTACK_TERRITORY;
    }
}
