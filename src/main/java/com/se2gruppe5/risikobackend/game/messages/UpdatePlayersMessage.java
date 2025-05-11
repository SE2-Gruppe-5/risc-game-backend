package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public record UpdatePlayersMessage(Map<UUID, Player> players) implements Message {
    public UpdatePlayersMessage(List<Player> players) {
        this(players.stream().collect(Collectors.toMap(Player::getUuid, UnaryOperator.identity())));
    }

    @Override
    public MessageType getType() {
        return MessageType.UPDATE_PLAYERS;
    }
}
