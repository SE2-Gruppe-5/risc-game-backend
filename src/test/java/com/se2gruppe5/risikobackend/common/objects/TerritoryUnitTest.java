package com.se2gruppe5.risikobackend.common.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TerritoryUnitTest {
    private Territory t1;
    private Territory t2;
    private Territory t3;

    @BeforeEach
    public void setUp() {
        t1 = new Territory(1, null, 0, Continent.SOUTHBRIDGE);
        t2 = new Territory(2, null, 0, Continent.WIRELESS_MESH);
        t3 = new Territory(3, null, 0, Continent.CMOS);
    }

    @Test
    public void testTerritoriesConnected() {
        t1.getConnectionIds().add(t2.getId());
        t1.getConnectionIds().add(t3.getId());

        assertTrue(t1.isConnected(t2));
        assertTrue(t1.isConnected(t3));
    }

    @Test
    public void testTerritoriesNotConnected() {
        t1.getConnectionIds().add(t2.getId());
        t2.getConnectionIds().add(t3.getId());

        assertFalse(t1.isConnected(t3));
        assertFalse(t3.isConnected(t1));
    }
}
