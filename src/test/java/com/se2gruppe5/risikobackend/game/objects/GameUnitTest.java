package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameUnitTest {
    private UUID player1Id;
    private UUID player2Id;
    private ConcurrentHashMap<UUID, Player> players;
    private ArrayList<Territory> customTerritories;
    private Game game;

    @BeforeEach
    void setup() {
        UUID gameId = UUID.randomUUID();
        customTerritories = new ArrayList<>();
        //Initialize Players
        player1Id = UUID.randomUUID();
        player2Id = UUID.randomUUID();
        Player player1 = new Player(player1Id, "Markus", 0xFF0000);
        Player player2 = new Player(player2Id, "Leo", 0x00FF00);
        players = new ConcurrentHashMap<>();
        players.put(player1Id, player1);
        players.put(player2Id, player2);

        //Create Game
        game = new Game(gameId, players, customTerritories);
    }

    @Test
    void testConstructorAndStartInitializesCorrectly() {
        game.start();

        assertEquals(0, game.getPhaseIndex());
        assertFalse(game.getRequiresPlayerChange());

        assertTrue(game.getPlayerTurnOrder().getFirst().isCurrentTurn());
        assertFalse(game.getPlayerTurnOrder().getLast().isCurrentTurn());
    }

    @Test
    void phaseOrderTest() {
        game.start();

        //Nach setup initial phase #1
        assertEquals(0, game.getPhaseIndex());
        assertFalse(game.getRequiresPlayerChange());

        //nextPhase -> phase #2
        game.nextPhase();
        assertEquals(1, game.getPhaseIndex());
        assertFalse(game.getRequiresPlayerChange());

        //nextPhase -> phase #3
        game.nextPhase();
        assertEquals(2, game.getPhaseIndex());
        assertFalse(game.getRequiresPlayerChange());

        //nextPhase -> wrap around back to phase #1 + playerchange required
        game.nextPhase();
        assertEquals(0, game.getPhaseIndex());
        assertTrue(game.getRequiresPlayerChange());
    }

    @Test
    void playerTurnOrderTest() {
        game.start();

        //Nach Setup p1 turn
        assertTrue(game.getPlayerTurnOrder().getFirst().isCurrentTurn());
        assertFalse(game.getPlayerTurnOrder().getLast().isCurrentTurn());
        game.nextPlayer();

        //NextPlayer -> p2 turn
        assertFalse(game.getPlayerTurnOrder().getFirst().isCurrentTurn());
        assertTrue(game.getPlayerTurnOrder().getLast().isCurrentTurn());

        //NextPlayer -> wraparound zurück zu p1
        game.nextPlayer();
        assertTrue(game.getPlayerTurnOrder().getFirst().isCurrentTurn());
        assertFalse(game.getPlayerTurnOrder().getLast().isCurrentTurn());
    }

    @Test
    void temporaryTerritoryDivideTest() { //todo must most likely be changed

        assertDoesNotThrow(() -> game.assignTerritories());
        game.getTerritories().forEach(t ->
                assertTrue(players.containsKey(t.getOwner()))
        );

        customTerritories.add(new Territory(1, player1Id, 1, Continent.CMOS));
        customTerritories.add(new Territory(1, player2Id, 2, Continent.DCON));
        customTerritories.add(new Territory(1, player1Id, 3, Continent.MMC));


        assertThrows(IllegalStateException.class, () -> game.assignTerritories());
    }


    @Test
    void startingTroopDistributionTest() {
        customTerritories.add(new Territory(1, UUID.randomUUID(), 11, Continent.EMBEDDED_CONTROLLER));
        customTerritories.add(new Territory(2, UUID.randomUUID(), 22, Continent.ESSENTIALS));

        assertEquals(customTerritories, game.getTerritories());
        assertDoesNotThrow(() -> game.distributeStartingTroops(5));
        game.getTerritories().forEach(t -> assertTrue(t.getStat() >= 1));
    }
}
