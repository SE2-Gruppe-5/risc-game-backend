package com.se2gruppe5.risikobackend.lobby.services;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.services.GameService;
import com.se2gruppe5.risikobackend.lobby.messages.GameStartMessage;
import com.se2gruppe5.risikobackend.lobby.messages.JoinLobbyMessage;
import com.se2gruppe5.risikobackend.lobby.messages.LeaveLobbyMessage;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.repositories.LobbyRepository;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceUnitTest {
    LobbyRepository lobbyRepository;
    SseBroadcastService sseBroadcaster;
    GameService gameService;
    LobbyService lobbyService;

    @BeforeEach
    void setup() {
        lobbyRepository = Mockito.mock(LobbyRepository.class);
        sseBroadcaster = Mockito.mock(SseBroadcastService.class);
        gameService = Mockito.mock(GameService.class);
        lobbyService = new LobbyService(lobbyRepository, sseBroadcaster, gameService);
    }

    @Test
    void testCreateLobby() {
        lobbyService.createLobby();
        Mockito.verify(lobbyRepository, Mockito.times(1)).addLobby(Mockito.any());
    }

    @Test
    void testDeleteLobby() {
        String lobbyId = "testLobbyId";
        Lobby lobby = new Lobby(lobbyId);

        UUID uuid = UUID.randomUUID();
        lobby.players().put(uuid, new Player(uuid, "testPlayerName", 0));

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(lobby);
        assertDoesNotThrow(() -> lobbyService.deleteLobby(lobbyId));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
        Mockito.verify(sseBroadcaster, Mockito.times(1)).send(uuid, new LeaveLobbyMessage(uuid, "Lobby deleted"));
        Mockito.verify(lobbyRepository, Mockito.times(1)).removeLobby(lobbyId);
    }

    @Test
    void testDeleteInvalidLobby() {
        String lobbyId = "testLobbyId";

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> lobbyService.deleteLobby(lobbyId));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
    }

    @Test
    void testJoinLobby() {
        String lobbyId = "testLobbyId";
        Lobby lobby = new Lobby(lobbyId);

        UUID uuid = UUID.randomUUID();
        Player player = new Player(uuid, "testPlayerName", 0);
        lobby.players().put(uuid, player);

        UUID newUuid = UUID.randomUUID();
        Player newPlayer = new Player(newUuid, "testNewPlayerName", 0);

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(lobby);
        assertDoesNotThrow(() -> lobbyService.joinLobby(lobbyId, newPlayer));
        assertEquals(2, lobby.players().size());
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
        Mockito.verify(sseBroadcaster, Mockito.times(1)).broadcast(lobby, new JoinLobbyMessage(newUuid, newPlayer.getName(), lobbyId));
        Mockito.verify(sseBroadcaster, Mockito.times(1)).send(newUuid, new JoinLobbyMessage(uuid, player.getName(), lobbyId));
    }

    @Test
    void testJoinInvalidLobby() {
        String lobbyId = "testLobbyId";
        Player player = new Player(UUID.randomUUID(), "testPlayerName", 0);

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> lobbyService.joinLobby(lobbyId, player));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
    }

    @Test
    void testJoinAlreadyInLobby() {
        String lobbyId = "testLobbyId";
        Lobby lobby = new Lobby(lobbyId);

        UUID uuid = UUID.randomUUID();
        Player player = new Player(uuid, "testPlayerName", 0);
        lobby.players().put(uuid, player);

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(lobby);
        assertThrows(IllegalStateException.class, () -> lobbyService.joinLobby(lobbyId, player));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
    }

    @CsvSource(value = {"testReason", "null"}, nullValues = {"null"})
    @ParameterizedTest
    void testLeaveLobby(String reason) {
        String lobbyId = "testLobbyId";
        Lobby lobby = new Lobby(lobbyId);

        UUID uuid = UUID.randomUUID();
        Player player = new Player(uuid, "testPlayerName", 0);
        lobby.players().put(uuid, player);

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(lobby);
        assertDoesNotThrow(() -> lobbyService.leaveLobby(lobbyId, uuid, reason));
        assertTrue(lobby.players().isEmpty());
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
        Mockito.verify(sseBroadcaster, Mockito.times(1)).broadcast(lobby, new LeaveLobbyMessage(uuid, reason));
    }

    @Test
    void testLeaveInvalidLobby() {
        String lobbyId = "testLobbyId";
        UUID uuid = UUID.randomUUID();

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> lobbyService.leaveLobby(lobbyId, uuid, "testReason"));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
    }

    @Test
    void testStartGame() {
        String lobbyId = "testLobbyId";
        Lobby lobby = new Lobby(lobbyId);

        UUID uuid1 = UUID.randomUUID();
        Player player1 = new Player(uuid1, "testPlayer1Name", 0);

        UUID uuid2 = UUID.randomUUID();
        Player player2 = new Player(uuid2, "testPlayer2Name", 0);
        lobby.players().put(uuid1, player1);
        lobby.players().put(uuid2, player2);

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(lobby);
        Mockito.when(gameService.createGame(lobby)).thenReturn(new Game(UUID.randomUUID(), lobby.players(), new ArrayList<>()));
        assertDoesNotThrow(() -> lobbyService.startGame(lobbyId));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
        Mockito.verify(gameService, Mockito.times(1)).createGame(lobby);
        Mockito.verify(lobbyRepository, Mockito.times(1)).removeLobby(lobbyId);
        Mockito.verify(sseBroadcaster, Mockito.times(1)).broadcast(Mockito.eq(lobby), Mockito.any(GameStartMessage.class));
    }

    @Test
    void testStartGameFromInvalidLobby() {
        String lobbyId = "testLobbyId";

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> lobbyService.startGame(lobbyId));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
    }

    @Test
    void testStartGameWithNotEnoughPlayers() {
        String lobbyId = "testLobbyId";
        Lobby lobby = new Lobby(lobbyId);

        Mockito.when(lobbyRepository.getLobby(lobbyId)).thenReturn(lobby);
        assertThrows(IllegalStateException.class, () -> lobbyService.startGame(lobbyId));
        Mockito.verify(lobbyRepository, Mockito.times(1)).getLobby(lobbyId);
    }
}
