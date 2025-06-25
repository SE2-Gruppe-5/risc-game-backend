package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Player;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GamePhaseUnitTest {
    private static final int PHASES = 3;
    private static final boolean[] PHASE_REQUIRES_PLAYER_CHANGE = {false, false, false, true};
    private static Game game;

    @BeforeAll
    static void setup() {
        ConcurrentHashMap<UUID, Player> players = new ConcurrentHashMap<>();
        addRandomPlayer(players, "Markus", 0xFF0000);
        addRandomPlayer(players, "Leo", 0x00FF00);
        game = new Game(UUID.randomUUID(), players, new ArrayList<>());
    }

    static void addRandomPlayer(Map<UUID, Player> map, String name, int color) {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, name, color);
        map.put(playerId, player);
    }

    @Order(1)
    @Test
    void testStartInReinforcePhase() {
        game.start();

        assertPhase(0);
    }

    @Order(2)
    @Test
    void testEnterAttackPhase() {
        game.nextPhase();

        assertPhase(1);
    }

    @Order(3)
    @Test
    void testEnterTradePhase() {
        game.nextPhase();

        assertPhase(2);
    }

    @Order(4)
    @Test
    void testGotoNextPlayer() {
        game.nextPhase();

        assertPhase(3);
    }

    private void assertPhase(int idx) {
        assertEquals(idx % PHASES, game.getPhaseIndex());
        assertEquals(PHASE_REQUIRES_PLAYER_CHANGE[idx], game.getRequiresPlayerChange());
    }
}
