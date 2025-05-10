package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record Game(UUID uuid, ConcurrentHashMap<UUID, Player> players, List<Territory> territories) {
    public Game(UUID uuid) {
        this(uuid, new ConcurrentHashMap<>(), new ArrayList<Territory>());
    }
    public Game(UUID uuid, ConcurrentHashMap<UUID, Player> players) {
        this(uuid, players, new ArrayList<Territory>());
    }
    public void start() {
        //TODO
    }

}
