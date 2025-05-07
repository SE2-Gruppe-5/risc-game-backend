package com.se2gruppe5.risikobackend.TroopCount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TerritoryManagerTest {

    private TerritoryManager manager;

    @BeforeEach
    void setUp() {
        manager = new TerritoryManager();
    }

    @Test
    void testGetAllTerritories() {
        List<TerritoryRecord> all = manager.getAllTerritories();
        assertEquals(3, all.size());
    }

    @Test
    void testGetTerritoriesOwnedByPlayer() {
        List<TerritoryRecord> owned = manager.getTerritoriesOwnedByPlayer("player1");
        assertEquals(2, owned.size());
    }

    @Test
    void testGetTerritoryById() {
        TerritoryRecord territory = manager.getTerritory(1);
        assertNotNull(territory);
        assertEquals("player1", territory.getOwner());
    }

    @Test
    void testUpdateTroops() {
        manager.updateTroops(1, 42);
        assertEquals(42, manager.getTerritory(1).getTroops());
    }

    @Test
    void testUpdateTroopsInvalidId() {
        manager.updateTroops(99, 42); // should not throw
        assertNull(manager.getTerritory(99));
    }
}
