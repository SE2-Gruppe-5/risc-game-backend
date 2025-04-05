package com.se2gruppe5.risikobackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.se2gruppe5.risikobackend.PlayerControllers.PlayerService;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTests {
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerService();
    }

    @Test
    void testAddActivePlayer() {
        playerService.addActivePlayer("Player1");

        assertTrue(playerService.getActivePlayers().contains("Player1"));
    }

    @Test
    void testAddActivePlayer_InvalidName() {
        assertThrows(IllegalArgumentException.class, () -> playerService.addActivePlayer(""));
    }

    @Test
    void testAddMultiplePlayers() {
        playerService.addActivePlayer("Player1");
        playerService.addActivePlayer("Player2");

        assertTrue(playerService.getActivePlayers().contains("Player1"));
        assertTrue(playerService.getActivePlayers().contains("Player2"));
        assertEquals(2, playerService.getActivePlayers().size());
    }

    @Test
    void testRegisterClientReturnsEmitter() {
        assertNotNull(playerService.registerClient());
    }

    @Test
    void testDuplicatePlayerNames() {
        playerService.addActivePlayer("Player1");
        playerService.addActivePlayer("Player1");

        assertEquals(1, playerService.getActivePlayers().size());
    }
}