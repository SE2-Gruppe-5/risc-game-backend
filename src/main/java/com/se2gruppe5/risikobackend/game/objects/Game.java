package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record Game(UUID uuid, ConcurrentHashMap<UUID, Player> players) {
    public void start() {

    }
}
