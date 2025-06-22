package com.se2gruppe5.risikobackend.cheatservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.common.objects.helpers.Position;
import com.se2gruppe5.risikobackend.common.objects.helpers.Size;
import com.se2gruppe5.risikobackend.game.objects.Game;

class GameTest {

    private Game game;
    private UUID player1Id;
    private UUID player2Id;
    private Player player1;
    private Player player2;

    private static final int DEFAULT_STAT = 1;
    private static final Continent DEFAULT_CONTINENT = Continent.POWER_SUPPLY;
    private static final Position DEFAULT_POSITION = new Position(0, 0);
    private static final Size DEFAULT_HEIGHT_WIDTH = new Size(1, 1);

    // Hilfsmethode für Territories
    private Territory createTerritory(UUID owner, int id) {
        return new Territory(id, owner, DEFAULT_STAT, DEFAULT_CONTINENT, DEFAULT_POSITION, DEFAULT_HEIGHT_WIDTH);
    }

    @BeforeEach
    void setUp() {
        player1Id = UUID.randomUUID();
        player2Id = UUID.randomUUID();
        player1 = new Player(player1Id, "Player 1",1);
        player2 = new Player(player2Id, "Player 2",2);

        ConcurrentHashMap<UUID, Player> players = new ConcurrentHashMap<>();
        players.put(player1Id, player1);
        players.put(player2Id, player2);

        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(createTerritory(player1Id, 1)); // owned by player1
        territories.add(createTerritory(player2Id, 2)); // owned by player2
        territories.add(createTerritory(player2Id, 3)); // owned by player2

        game = new Game(UUID.randomUUID(), players, territories);
    }

    @Test
    void testEnableCheatMode() {
        assertFalse(game.isCheatMode());
        game.enableCheatMode();
        assertTrue(game.isCheatMode());
    }

    @Test
    void testAreNeighborsTrue() {
        Territory a = createTerritory(player1Id, 1);
        Territory b = createTerritory(player2Id, 2);
        assertTrue(game.areNeighbors(a, b));
    }

    @Test
    void testAreNeighborsFalse() {
        Territory a = createTerritory(player1Id, 1);
        Territory c = createTerritory(player2Id, 3);
        assertFalse(game.areNeighbors(a, c));
    }

    @Test
    void testAreNeighborsBidirectional() {
        Territory a = createTerritory(player1Id, 2);
        Territory b = createTerritory(player2Id, 1);
        assertTrue(game.areNeighbors(a, b));
    }

    @Test
    void testCheatConquerSuccessful() {
        game.enableCheatMode();

        // player1 owns territory 1, neighbor to territory 2
        game.cheatConquer(player1Id, 2);
        Territory conquered = game.getTerritories().stream()
                .filter(t -> t.getId() == 2)
                .findFirst()
                .orElseThrow();

        assertEquals(player1Id, conquered.getOwner());
    }

    @Test
    void testCheatConquerThrowsWhenNotNeighbor() {
        game.enableCheatMode();

        // territory 3 is not a neighbor of territory 1
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                game.cheatConquer(player1Id, 3));

        assertTrue(ex.getMessage().contains("not neighboring"));
    }

    @Test
    void testCheatConquerThrowsWhenCheatModeDisabled() {
        Exception ex = assertThrows(IllegalStateException.class, () ->
                game.cheatConquer(player1Id, 2));
        assertTrue(ex.getMessage().contains("Cheat mode is not enabled"));
    }

    @Test
    void testCheatConquerThrowsWhenPlayerInvalid() {
        game.enableCheatMode();
        UUID invalidPlayerId = UUID.randomUUID();

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                game.cheatConquer(invalidPlayerId, 2));

        assertTrue(ex.getMessage().contains("Player does not exist"));
    }

    @Test
    void testCheatConquerThrowsWhenTargetInvalid() {
        game.enableCheatMode();

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                game.cheatConquer(player1Id, 999)); // nonexistent ID

        assertTrue(ex.getMessage().contains("Territory ID invalid."));
    }
    @Test
    void testAreNeighbors_NewNeighborPairs() {
        Territory t9 = createTerritory(player1Id, 9);
        Territory t20 = createTerritory(player2Id, 20);
        Territory t30 = createTerritory(player1Id, 30);
        Territory t32 = createTerritory(player2Id, 32);
        Territory t25 = createTerritory(player1Id, 25);
        Territory t48 = createTerritory(player2Id, 48);

        assertTrue(game.areNeighbors(t9, t20));
        assertTrue(game.areNeighbors(t30, t32));
        assertTrue(game.areNeighbors(t25, t48));
    }

    @Test
    void testAreNeighbors_NonNeighbors() {
        Territory t1 = createTerritory(player1Id, 1);
        Territory t5 = createTerritory(player2Id, 5);
        Territory t10 = createTerritory(player1Id, 10);
        Territory t20 = createTerritory(player2Id, 20);

        assertFalse(game.areNeighbors(t1, t5));
        assertFalse(game.areNeighbors(t10, t20)); // 10 hat 9,14 als Nachbarn, nicht 20
    }
    @Test
    void testCheatConquerFailsIfTargetOwnedByPlayerButNotNeighbor() {
        game.enableCheatMode();

        // player2 owns 3; player1 does not border it
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                game.cheatConquer(player1Id, 3));

        assertTrue(ex.getMessage().contains("not neighboring"));
    }
}
