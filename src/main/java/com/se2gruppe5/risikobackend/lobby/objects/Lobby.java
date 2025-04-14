package com.se2gruppe5.risikobackend.lobby.objects;

import com.se2gruppe5.risikobackend.common.objects.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record Lobby(String code, ConcurrentHashMap<UUID, Player> players) {
    public static final int CODE_LENGTH = 4;

    public Lobby(String code) {
        this(code, new ConcurrentHashMap<>());
    }
}
