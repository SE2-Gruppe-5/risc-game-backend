package com.se2gruppe5.risikobackend.game.controllers;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.game.messages.*;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.services.GameService;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GameControllerUnitTest {
    private GameController gameController;
    private GameService gameService;
    private SseBroadcastService sseBroadcastService;
    private UUID gameId;
    private UUID playerId;
    private Territory territory;
    private Player player;
    private Game dummyGame;

    @BeforeEach
    void setup() {
        gameService = Mockito.mock(GameService.class);
        territory = Mockito.mock(Territory.class);
        player = Mockito.mock(Player.class);

        // use a field dummyGame so we can verify broadcasts against it
        dummyGame = Mockito.mock(Game.class);
        when(gameService.getGame(any(UUID.class))).thenReturn(dummyGame);
        when(gameService.getPlayers(any(UUID.class))).thenReturn(new java.util.concurrent.ConcurrentHashMap<>());
        when(gameService.getTerritoryList(any(UUID.class))).thenReturn(new java.util.ArrayList<>());
        when(gameService.getTerritoryList(any(UUID.class))).thenReturn(new java.util.ArrayList<>());
        when(gameService.getPhase(any(UUID.class))).thenReturn(0);
        when(gameService.getTerritory(any(UUID.class), anyInt())).thenReturn(territory);
        when(gameService.getPlayer(any(UUID.class), any(UUID.class))).thenReturn(player);
        sseBroadcastService = Mockito.mock(SseBroadcastService.class);
        gameController = new GameController(gameService, sseBroadcastService);
        gameId = UUID.randomUUID();
        playerId = UUID.randomUUID();
    }

    @Test
    void testUpdatePlayerSuccess() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);
        gameController.updatePlayer(gameId, playerId, "name", 1);

        verify(player, times(1)).setName("name");
        verify(player, times(1)).setColor(1);

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
    void testChangePhaseToNextPlayerSuccess() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);
        when(gameService.checkRequiresPlayerChange(gameId)).thenReturn(true);
        gameController.changePhase(gameId);

        verify(gameService, times(1)).nextPhase(gameId);
        verify(gameService, times(1)).nextPlayer(gameId);
        verify(sseBroadcastService, times(1))
                .broadcast(eq(dummyGame), any(UpdatePlayersMessage.class));
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

        verify(territory, times(1)).setOwner(playerId);
        verify(territory, times(1)).setStat(10);

        verify(sseBroadcastService, times(1))
                .broadcast(eq(dummyGame), any(ChangeTerritoryMessage.class));
    }


    @Test
    void testAbandonGame() {
        when(gameService.getPlayer(gameId, playerId)).thenReturn(new Player(playerId, "TestPlayer", 0));
        when(sseBroadcastService.hasSink(playerId)).thenReturn(true);

        assertDoesNotThrow(() -> gameController.killPlayer(gameId, playerId));
        verify(gameService).getGame(eq(gameId));
        verify(gameService).getPlayer(eq(gameId), eq(playerId));
        verify(sseBroadcastService).broadcast(eq(dummyGame), any(UpdatePlayersMessage.class));
    }

    @Test
    void testChangeTerritoryChecksWinner() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);
        when(gameService.checkWon(any())).thenReturn(playerId);
        gameController.changeTerritory(gameId, playerId, 5, 10);

        verify(sseBroadcastService, times(1))
                .broadcast(eq(dummyGame), any(PlayerWonMessage.class));
    }

    @Test
    void testAttackTerritoryCorrectPlayerNotified() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);

        UUID playerId1 = UUID.randomUUID();
        UUID playerId2 = UUID.randomUUID();
        Territory t1 = new Territory(1, playerId1, 2, Continent.DCON);
        Territory t2 = new Territory(2, playerId2, 2, Continent.CMOS);
        when(gameService.getTerritory(any(), eq(1))).thenReturn(t1);
        when(gameService.getTerritory(any(), eq(2))).thenReturn(t2);

        gameController.attackTerritory(UUID.randomUUID(), t1.getId(), t2.getId(), 5);
        verify(sseBroadcastService, times(1))
                .send(eq(playerId2), any(AttackTerritoryMessage.class));
    }

    @Test
    void testReportDiceStatus() {
        when(sseBroadcastService.hasSink(gameId)).thenReturn(true);

        UUID playerId = UUID.randomUUID();
        gameController.reportDiceStatus(UUID.randomUUID(), playerId, List.of(1));
        verify(sseBroadcastService, times(1))
                .send(eq(playerId), any(ReportDiceStatusMessage.class));
    }

    @Test
    void testCheatingAccusation() {
        gameController.getCheatingInfo(gameId, playerId);
        verify(gameService).getGame(gameId);
        verify(sseBroadcastService).broadcast(eq(dummyGame), eq(new CheatAccusationMessage(playerId)));
    }
}
