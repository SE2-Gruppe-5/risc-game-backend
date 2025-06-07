package com.se2gruppe5.risikobackend.game.services;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.repositories.GameRepository;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceUnitTest {
    private GameRepository gameRepository;
    private GameService gameService;
    private UUID gameUUID;
    private Game mockGame;

    @BeforeEach
    void setup() {
        gameRepository = Mockito.mock(GameRepository.class);
        gameService = new GameService(gameRepository);
        gameUUID = UUID.randomUUID();
        mockGame = Mockito.mock(Game.class);
    }

    @Test
    void createGameToRepoTest() {
        when(gameRepository.hasGame(any(UUID.class))).thenReturn(false);
        Lobby lobby = new Lobby("lobby1");
        UUID player1UUID = UUID.randomUUID();
        UUID player2UUID = UUID.randomUUID();
        lobby.players().put(player1UUID, new Player(player1UUID, "Markus", 0x000000));
        lobby.players().put(player2UUID, new Player(player2UUID, "Leo", 0x000000));

        Game created = gameService.createGame(lobby);

        //Check whether game is added to repository
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository, times(1)).addGame(captor.capture());
        Game saved = captor.getValue();
        assertEquals(saved, created);

        //Check whether all players were added
        assertEquals(2, created.getPlayers().size());
        assertTrue(created.getPlayers().containsKey(player1UUID));
        assertTrue(created.getPlayers().containsKey(player2UUID));
    }

    @Test
    void getGameFromRepoTest() {
        when(gameRepository.getGame(gameUUID)).thenReturn(mockGame);
        Game result = gameService.getGameById(gameUUID);
        assertSame(mockGame, result);
        verify(gameRepository, times(1)).getGame(gameUUID);
    }

    @Test
    void requiresPlayerCheckFlagTest() {
        when(gameRepository.getGame(gameUUID)).thenReturn(mockGame);
        when(mockGame.getRequiresPlayerChange()).thenReturn(true);
        assertTrue(gameService.checkRequiresPlayerChange(gameUUID));
        verify(mockGame, times(1)).getRequiresPlayerChange();
    }

    @Test
    void getTerritoryTest() {
        Territory t = new Territory(UUID.randomUUID(), 11, 1);

        ArrayList<Territory> territoryList = new ArrayList<>();
        territoryList.add(t);
        
        when(gameRepository.getGame(gameUUID)).thenReturn(mockGame);
        when(mockGame.getTerritories()).thenReturn(territoryList);
        assertEquals(territoryList, gameService.getTerritoryList(gameUUID));
    }

    @Test
    void getPlayersTest() {
        Player player1 = new Player(UUID.randomUUID(), "Elias", 0x000000);

        ConcurrentHashMap<UUID, Player> playerList = new ConcurrentHashMap<>();
        playerList.put(player1.getId(), player1);

        when(gameRepository.getGame(gameUUID)).thenReturn(mockGame);
        when(mockGame.getPlayers()).thenReturn(playerList);
        assertEquals(playerList, gameService.getPlayers(gameUUID));
    }

    @Test
    void phaseAndPlayerCalledTest() {
        
        int phaseInt = 123;
        when(gameRepository.getGame(gameUUID)).thenReturn(mockGame);
        when(mockGame.getPhaseIndex()).thenReturn(phaseInt);

        gameService.nextPhase(gameUUID);
        verify(mockGame, times(1)).nextPhase();

        gameService.nextPlayer(gameUUID);
        verify(mockGame, times(1)).nextPlayer();

        int phase = gameService.getPhase(gameUUID);
        assertEquals(phaseInt, phase);
    }

    @Test
    void gameNotFoundThrowTest() {
        when(gameRepository.getGame(gameUUID)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> gameService.nextPhase(gameUUID));
        assertThrows(IllegalArgumentException.class, () -> gameService.getPhase(gameUUID));
    }
}
