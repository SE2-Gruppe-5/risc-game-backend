package com.se2gruppe5.risikobackend.game.controllers;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.game.messages.ChangeTerritoryMessage;
import com.se2gruppe5.risikobackend.game.messages.UpdatePhaseMessage;
import com.se2gruppe5.risikobackend.game.messages.UpdatePlayersMessage;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.services.GameService;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GameControllerUnitTest {
    private GameController gameController;
    private GameService gameService;
    private SseBroadcastService sseBroadcastService;
    private UUID gameId;
    private UUID playerId;
    private Game dummyGame;

    @BeforeEach
    void setup() {
        gameService = Mockito.mock(GameService.class);

        // use a field dummyGame so we can verify broadcasts against it
        dummyGame = Mockito.mock(Game.class);
        when(gameService.getGameById(any(UUID.class))).thenReturn(dummyGame);
        when(gameService.getPlayers(any(UUID.class))).thenReturn(new java.util.concurrent.ConcurrentHashMap<>());
        when(gameService.getTerritoryList(any(UUID.class))).thenReturn(new java.util.ArrayList<>());
        when(gameService.getTerritoryList(any(UUID.class))).thenReturn(new java.util.ArrayList<>());
        when(gameService.getPhase(any(UUID.class))).thenReturn(0);
        sseBroadcastService = Mockito.mock(SseBroadcastService.class);
        gameController = new GameController(gameService, sseBroadcastService);
        gameId = UUID.randomUUID();
        playerId = UUID.randomUUID();
    }

    @Test
    void testUpdatePlayerSuccess() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);
        gameController.updatePlayer(gameId, playerId, "name", 1);

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(gameService, times(1)).updatePlayer(eq(gameId), playerCaptor.capture());
        Player captured = playerCaptor.getValue();
        assertEquals(playerId, captured.getId());
        assertEquals("name", captured.getName());
        assertEquals(1, captured.getColor());

        // broadcast should be called with the Game and the proper message
        verify(sseBroadcastService, times(1))
                .broadcast(eq(dummyGame), any(UpdatePlayersMessage.class));
    }



    @Test
    void testChangePhaseSuccess() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);
        when(gameService.checkRequiresPlayerChange(gameId)).thenReturn(false);
        gameController.changePhase(gameId);

        verify(gameService, times(1)).nextPhase(gameId);
        verify(sseBroadcastService, times(1))
                .broadcast(eq(dummyGame), any(UpdatePhaseMessage.class));
    }



    @Test
    void testGetGameInfoSuccess() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);
        gameController.getGameInfo(gameId, playerId);

        verify(sseBroadcastService, times(1))
                .send(eq(playerId), any(ChangeTerritoryMessage.class));
        verify(sseBroadcastService, times(1))
                .send(eq(playerId), any(UpdatePlayersMessage.class));
        verify(sseBroadcastService, times(1))
                .broadcast(eq(dummyGame), any(UpdatePhaseMessage.class));
    }



    @Test
    void testChangeTerritorySuccess() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);
        gameController.changeTerritory(gameId, playerId, 5, 10);

        ArgumentCaptor<Territory> territoryCaptor = ArgumentCaptor.forClass(Territory.class);
        verify(gameService, times(1))
                .changeTerritory(eq(gameId), territoryCaptor.capture());
        Territory capturedTerr = territoryCaptor.getValue();
        assertEquals(playerId, capturedTerr.owner());
        assertEquals(10, capturedTerr.stat());
        assertEquals(5, capturedTerr.id());

        verify(sseBroadcastService, times(1))
                .broadcast(eq(dummyGame), any(ChangeTerritoryMessage.class));
    }
}
