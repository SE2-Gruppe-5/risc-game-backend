package com.se2gruppe5.risikobackend.game.repositories;

import com.se2gruppe5.risikobackend.game.objects.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameRepositoryImplUnitTest {
    /*
    GameRepository gameRepository;

    @BeforeEach
    void setup() {
        gameRepository = new GameRepositoryImpl();
    }

    @Test
    void testAddAndRemoveGame() {
        UUID uuid = UUID.randomUUID();
        Game game = new Game(uuid);

        gameRepository.removeGame(uuid);
        assertFalse(gameRepository.hasGame(uuid));

        gameRepository.addGame(game);
        assertTrue(gameRepository.hasGame(uuid));

        gameRepository.removeGame(uuid);
        assertFalse(gameRepository.hasGame(uuid));
    }

    @Test
    void testGetGame() {
        UUID uuid = UUID.randomUUID();
        Game game = new Game(uuid);
        gameRepository.addGame(game);

        assertEquals(game, gameRepository.getGame(uuid));

        gameRepository.removeGame(uuid);
        assertNull(gameRepository.getGame(uuid));
    }

     */
}
