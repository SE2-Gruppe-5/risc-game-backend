package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import com.se2gruppe5.risikobackend.common.objects.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;
    private UUID gameId;
    private Player player1;
    private Territory territoryA;
    private Territory territoryB;

    @BeforeEach
    void setUp() {
        gameId = UUID.randomUUID();
        game = new Game(gameId);
        player1 = new Player( UUID.randomUUID(), "Alice");
        territoryA = new Territory("A", 0);
        territoryB = new Territory("B", 0);
    }

    @Test
    void testAddPlayerAndGetPlayers() {
        game.addPlayer(player1);
        List<String> players = game.getPlayers();
        assertEquals(1, players.size());
        assertTrue(players.contains("Alice"));
    }

    @Test
    void testAddTerritoryAndGetTerritories() {
        game.addTerritory(territoryA);
        game.addTerritory(territoryB);
        List<String> territories = game.getTerritories();
        assertEquals(2, territories.size());
        assertTrue(territories.contains("A"));
        assertTrue(territories.contains("B"));
    }

    @Test
    void testAssignTerritories() {
        game.addTerritory(territoryA);
        game.addTerritory(territoryB);
        game.addPlayer(player1);

        Map<UUID, List<String>> assignment = Map.of(player1.uuid(), List.of("A", "B"));
        game.assignTerritories(assignment);

        assertEquals(player1.uuid(), territoryA.getOwner());
        assertEquals(player1.uuid(), territoryB.getOwner());
    }

    @Test
    void testDistributeTroops() {
        territoryA.setOwner(player1.uuid());
        territoryB.setOwner(null); // no owner

        game.addTerritory(territoryA);
        game.addTerritory(territoryB);

        game.distributeTroops();

        assertEquals(1, territoryA.getStat());
        assertEquals(0, territoryB.getStat());
    }
}
