package com.se2gruppe5.risikobackend.cheatservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.se2gruppe5.risikobackend.game.controllers.GameController;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.services.GameService;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;

public class CheatconquerUnittests {

    private GameController gameController;
    private GameService gameService;
    private SseBroadcastService sseBroadcastService;
    private UUID gameId;
    private UUID playerId;
    private Game dummyGame;

    @BeforeEach
    void setup() {
        gameService = Mockito.mock(GameService.class);
        sseBroadcastService = Mockito.mock(SseBroadcastService.class);
        gameController = new GameController(gameService, sseBroadcastService);

        gameId = UUID.randomUUID();
        playerId = UUID.randomUUID();

        dummyGame = Mockito.mock(Game.class);
        when(gameService.getGameById(any(UUID.class))).thenReturn(dummyGame);
    }

    @Test
    void testCheatConquerSuccess() {
        // cheatConquer auf dem Service darf keine Exception werfen
        doNothing().when(gameService).cheatConquer(gameId, playerId, 5);

        ResponseEntity<?> response = gameController.cheatConquer(gameId, playerId, 5);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(gameService, times(1)).cheatConquer(gameId, playerId, 5);
    }

    @Test
    void testCheatConquerIllegalState() {
        doThrow(new IllegalStateException("Cheat mode is not enabled"))
                .when(gameService).cheatConquer(gameId, playerId, 7);

        ResponseEntity<?> response = gameController.cheatConquer(gameId, playerId, 7);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Cheat mode is not enabled", response.getBody());
    }

    @Test
    void testCheatConquerIllegalArgument() {
        doThrow(new IllegalArgumentException("Territory does not exist"))
                .when(gameService).cheatConquer(gameId, playerId, 99);

        ResponseEntity<?> response = gameController.cheatConquer(gameId, playerId, 99);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Territory does not exist", response.getBody());
    }
}
