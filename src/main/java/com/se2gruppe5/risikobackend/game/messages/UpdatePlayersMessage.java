package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record UpdatePlayersMessage(UUID gameId, ConcurrentHashMap<UUID, Player> players) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.UPDATE_PLAYERS;
    }
}
