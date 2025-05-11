package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TerritoryTest {

    @Test
    void testConstructorAndGetters() {
        Territory territory = new Territory("A", 5);
        assertEquals("A", territory.getId());
        assertEquals(5, territory.getStat());
        assertNull(territory.getOwner());
    }

    @Test
    void testSetStat() {
        Territory territory = new Territory("A", 1);
        territory.setStat(10);
        assertEquals(10, territory.getStat());
    }

    @Test
    void testSetOwner() {
        Territory territory = new Territory("A", 1);
        UUID playerId = UUID.randomUUID();
        territory.setOwner(playerId);
        assertEquals(playerId, territory.getOwner());
    }

    @Test
    void testSetters() {
        Territory territory = new Territory("Y", 0);
        territory.setStat(5);
        UUID ownerId = UUID.randomUUID();
        territory.setOwner(ownerId);

        assertEquals(5, territory.getStat());
        assertEquals(ownerId, territory.getOwner());
    }
}
