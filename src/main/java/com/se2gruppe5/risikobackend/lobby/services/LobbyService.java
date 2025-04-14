package com.se2gruppe5.risikobackend.lobby.services;

import com.se2gruppe5.risikobackend.lobby.messages.JoinLobbyMessage;
import com.se2gruppe5.risikobackend.lobby.messages.LeaveLobbyMessage;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.repositories.LobbyRepository;
import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import com.se2gruppe5.risikobackend.common.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LobbyService {
    private final SseBroadcastService sseBroadcastService;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(SseBroadcastService sseBroadcastService, LobbyRepository lobbyRepository) {
        this.sseBroadcastService = sseBroadcastService;
        this.lobbyRepository = lobbyRepository;
    }

    /**
     * Creates a new lobby
     *
     * @return {@link Lobby}
     */
    public Lobby createLobby() {
        String id = IdUtil.generateId(Lobby.CODE_LENGTH, lobbyRepository::hasLobby);
        Lobby lobby = new Lobby(id);
        lobbyRepository.addLobby(lobby);
        return lobby;
    }

    /**
     * Deletes a lobby
     *
     * @param id the lobby id
     */
    public void deleteLobby(String id) {
        Lobby lobby = lobbyRepository.getLobby(id);
        if (lobby == null) {
            throw new IllegalArgumentException("Lobby not found");
        }
        for (UUID uuid : lobby.players().keySet()) {
            sseBroadcastService.send(uuid, new LeaveLobbyMessage(uuid));
        }
        lobbyRepository.removeLobby(id);
    }

    /**
     * Joins a player to a lobby
     *
     * @param id the lobby id
     * @param player the player to join
     */
    public void joinLobby(String id, Player player) {
        Lobby lobby = lobbyRepository.getLobby(id);
        if (lobby == null) {
            throw new IllegalArgumentException("Lobby not found");
        }
        if (lobby.players().contains(player)) {
            throw new IllegalStateException("Player already in lobby");
        }
        lobby.players().put(player.uuid(), player);
        sseBroadcastService.broadcast(lobby, new JoinLobbyMessage(player.uuid(), player.name(), id));
    }

    /**
     * Leaves a player from a lobby
     *
     * @param id the lobby id
     * @param playerId the player id
     */
    public void leaveLobby(String id, UUID playerId) {
        Lobby lobby = lobbyRepository.getLobby(id);
        if (lobby == null) {
            throw new IllegalArgumentException("Lobby not found");
        }
        lobby.players().remove(playerId);
        sseBroadcastService.broadcast(lobby, new LeaveLobbyMessage(playerId));
    }
}
