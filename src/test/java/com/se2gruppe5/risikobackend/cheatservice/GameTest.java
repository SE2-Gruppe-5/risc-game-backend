package com.se2gruppe5.risikobackend.cheatservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.game.objects.Game;

class GameTest {

    private Game game;
    private UUID player1Id;
    private UUID player2Id;
    private Player player1;
    private Player player2;

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
        territories.add(new Territory(player1Id, 5, 1)); // owned by player1
        territories.add(new Territory(player2Id, 3, 2)); // owned by player2
        territories.add(new Territory(player2Id, 3, 3)); // owned by player2

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
        Territory a = new Territory(player1Id, 5, 1);
        Territory b = new Territory(player2Id, 3, 2);
        assertTrue(game.areNeighbors(a, b));
    }

    @Test
    void testAreNeighborsFalse() {
        Territory a = new Territory(player1Id, 5, 1);
        Territory c = new Territory(player2Id, 3, 3);
        assertFalse(game.areNeighbors(a, c));
    }

    @Test
    void testCheatConquerSuccessful() {
        game.enableCheatMode();

        // player1 owns territory 1, neighbor to territory 2
        game.cheatConquer(player1Id, 2);
        Territory conquered = game.getTerritories().stream()
                .filter(t -> t.id() == 2)
                .findFirst()
                .orElseThrow();

        assertEquals(player1Id, conquered.owner());
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

        assertTrue(ex.getMessage().contains("Target territory does not exist"));
    }
}
