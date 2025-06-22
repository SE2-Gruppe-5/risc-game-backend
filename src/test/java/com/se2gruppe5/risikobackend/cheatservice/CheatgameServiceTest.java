package com.se2gruppe5.risikobackend.cheatservice;


import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.repositories.GameRepository;
import com.se2gruppe5.risikobackend.game.services.GameService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheatgameServiceTest {
    private GameRepository gameRepository;
    private GameService gameService;

    private Game mockGame;
    private UUID gameId;
    private UUID playerId;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameService = new GameService(gameRepository);

        gameId = UUID.randomUUID();
        playerId = UUID.randomUUID();

        mockGame = mock(Game.class);
        when(gameRepository.getGame(gameId)).thenReturn(mockGame);
    }

    @Test
    void cheatConquer_delegatesToGameSuccessfully() {
        int territoryId = 5;

        // Act
        gameService.cheatConquer(gameId, playerId, territoryId);

        // Assert
        verify(mockGame, times(1)).cheatConquer(playerId, territoryId);
    }

    @Test
    void cheatConquer_throwsWhenGameNotFound() {
        UUID nonexistentGameId = UUID.randomUUID();
        when(gameRepository.getGame(nonexistentGameId)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gameService.cheatConquer(nonexistentGameId, playerId, 42);
        });

        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    void cheatConquer_propagatesExceptionFromGame() {
        int territoryId = 42;
        doThrow(new IllegalStateException("Cheating not allowed"))
                .when(mockGame).cheatConquer(playerId, territoryId);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            gameService.cheatConquer(gameId, playerId, territoryId);
        });

        assertEquals("Cheating not allowed", exception.getMessage());
    }
}
