package com.se2gruppe5.risikobackend.lobby.repositories;

import com.se2gruppe5.risikobackend.common.repositories.Repository;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;

public interface LobbyRepository extends Repository<String, Lobby> {
    default void addLobby(Lobby lobby) {
        add(lobby.code(), lobby);
    }

    default void removeLobby(String id) {
        remove(id);
    }

    default boolean hasLobby(String id) {
        return has(id);
    }

    default Lobby getLobby(String id) {
        return get(id);
    }
}
