package com.se2gruppe5.risikobackend.lobby.controllers;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.services.LobbyService;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LobbyControllerUnitTest {
    private LobbyController lobbyController;
    private LobbyService lobbyService;
    private SseBroadcastService sseBroadcastService;

    @BeforeEach
    void setup() {
        lobbyService = Mockito.mock(LobbyService.class);
        sseBroadcastService = Mockito.mock(SseBroadcastService.class);
        lobbyController = new LobbyController(lobbyService, sseBroadcastService);
    }

    @Test
    void testCreateLobby() {
        Mockito.when(lobbyService.createLobby()).thenReturn(new Lobby("testLobbyId"));
        lobbyController.createLobby();
        Mockito.verify(lobbyService, Mockito.times(1)).createLobby();
    }

    @Test
    void testDeleteLobby() {
        String lobbyId = "testLobbyId";
        lobbyController.deleteLobby(lobbyId);
        Mockito.verify(lobbyService, Mockito.times(1)).deleteLobby(lobbyId);
    }

    @Test
    void testJoinLobby() {
        String lobbyId = "testLobbyId";
        UUID uuid = UUID.randomUUID();
        String playerName = "testPlayerName";

        Mockito.when(sseBroadcastService.hasSink(uuid)).thenReturn(true);
        assertDoesNotThrow(() -> lobbyController.joinLobby(lobbyId, uuid, playerName));
        Mockito.verify(lobbyService, Mockito.times(1)).joinLobby(lobbyId, new Player(uuid, playerName));
    }

    @Test
    void testJoinLobbyWithWrongUuid() {
        String lobbyId = "testLobbyId";
        UUID uuid = UUID.randomUUID();
        String playerName = "testPlayerName";

        Mockito.when(sseBroadcastService.hasSink(uuid)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> lobbyController.joinLobby(lobbyId, uuid, playerName));
        Mockito.verify(lobbyService, Mockito.times(0)).joinLobby(lobbyId, new Player(uuid, playerName));
    }

    @Test
    void testLeaveLobby() {
        String lobbyId = "testLobbyId";
        UUID uuid = UUID.randomUUID();
        lobbyController.leaveLobby(lobbyId, uuid);
        Mockito.verify(lobbyService, Mockito.times(1)).leaveLobby(lobbyId, uuid);
    }

    @Test
    void testStartGame() {
        String lobbyId = "testLobbyId";
        lobbyController.startGame(lobbyId);
        Mockito.verify(lobbyService, Mockito.times(1)).startGame(lobbyId);
    }
}
