
package com.se2gruppe5.risikobackend.game.repositories;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.game.objects.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryImplUnitTest {
    private GameRepositoryImpl repository;
    private ConcurrentHashMap<UUID, Player> players;

    @BeforeEach
    void setup() {
        repository = new GameRepositoryImpl();
        players = new ConcurrentHashMap<>();
        UUID p1UUID = UUID.randomUUID();
        Player p1 = new Player(p1UUID, "P1", 0xFFFFFF);
        UUID p2UUID = UUID.randomUUID();
        Player p2 = new Player(p2UUID, "P2", 0xFFFFFF);
        players.put(p1UUID, p1);
        players.put(p2UUID, p2);
    }

    @Test
    void testAddAndRemoveGame() {
        UUID id = UUID.randomUUID();
        Game game = new Game(id, players, new ArrayList<>());

        repository.remove(id);
        assertFalse(repository.has(id));

        repository.add(id, game);
        assertTrue(repository.has(id));

        repository.remove(id);
        assertFalse(repository.has(id));
    }

    @Test
    void testGetGame() {
        UUID id = UUID.randomUUID();
        Game game = new Game(id, players, new ArrayList<>());
        repository.add(id, game);

        assertEquals(game, repository.get(id));

        repository.remove(id);
        assertNull(repository.get(id));
    }
}
