package com.se2gruppe5.risikobackend.game.services;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.repositories.GameRepository;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceUnitTest {
    GameRepository gameRepository;
    GameService gameService;

    @BeforeEach
    void setup() {
        gameRepository = Mockito.mock(GameRepository.class);
        gameService = new GameService(gameRepository);
    }

    @Test
    void testCreateGame() {
        UUID uuid = UUID.randomUUID();
        Player player = new Player(uuid, "testPlayerName");

        Lobby lobby = new Lobby("testLobbyCode");
        lobby.players().put(uuid, player);

        Game game = gameService.createGame(lobby);
        assertEquals(lobby.players(), game.players());
        Mockito.verify(gameRepository).addGame(game);
    }
}
