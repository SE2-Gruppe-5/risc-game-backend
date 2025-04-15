package com.se2gruppe5.risikobackend.lobby.controllers;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.services.LobbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

class LobbyControllerUnitTest {
    private LobbyController lobbyController;
    private LobbyService lobbyService;

    @BeforeEach
    void setup() {
        lobbyService = Mockito.mock(LobbyService.class);
        lobbyController = new LobbyController(lobbyService);
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
        lobbyController.joinLobby(lobbyId, uuid, playerName);
        Mockito.verify(lobbyService, Mockito.times(1)).joinLobby(lobbyId, new Player(uuid, playerName));
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
