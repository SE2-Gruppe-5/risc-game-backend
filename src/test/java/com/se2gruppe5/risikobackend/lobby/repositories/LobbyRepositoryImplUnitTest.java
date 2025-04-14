package com.se2gruppe5.risikobackend.lobby.repositories;

import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyRepositoryImplUnitTest {
    LobbyRepository lobbyRepository;

    @BeforeEach
    void setup() {
        lobbyRepository = new LobbyRepositoryImpl();
    }

    @Test
    void testAddAndRemoveLobby() {
        Lobby lobby = new Lobby("testLobbyId");

        lobbyRepository.removeLobby(lobby.code());
        assertFalse(lobbyRepository.hasLobby("testLobbyId"));

        lobbyRepository.addLobby(lobby);
        assertTrue(lobbyRepository.hasLobby("testLobbyId"));

        lobbyRepository.removeLobby(lobby.code());
        assertFalse(lobbyRepository.hasLobby("testLobbyId"));
    }

    @Test
    void testGetLobby() {
        Lobby lobby = new Lobby("testLobbyId");
        lobbyRepository.addLobby(lobby);

        assertEquals(lobby, lobbyRepository.getLobby("testLobbyId"));

        lobbyRepository.removeLobby(lobby.code());
        assertNull(lobbyRepository.getLobby("testLobbyId"));
    }
}
