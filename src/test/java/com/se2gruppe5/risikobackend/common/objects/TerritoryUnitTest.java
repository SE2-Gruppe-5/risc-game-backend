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
    void setUp() {
        t1 = new Territory(1, null, 0, Continent.SOUTHBRIDGE);
        t2 = new Territory(2, null, 0, Continent.WIRELESS_MESH);
        t3 = new Territory(3, null, 0, Continent.CMOS);
    }

    @Test
    void testConnectedIdListConversion() {
        t1.getConnections().add(t2);
        t2.getConnections().add(t3);
        t1.connectionsToIds();
        t2.connectionsToIds();

        assertTrue(t1.getConnectionIds().contains(t2.getId()));
        assertTrue(t2.getConnectionIds().contains(t3.getId()));
    }

    @Test
    void testTerritoriesConnected() {
        t1.getConnections().add(t2);
        t1.getConnections().add(t3);

        assertTrue(t1.isConnected(t2));
        assertTrue(t1.isConnected(t3));
    }

    @Test
    void testTerritoriesNotConnected() {
        t1.getConnections().add(t2);
        t2.getConnections().add(t3);

        assertFalse(t1.isConnected(t3));
        assertFalse(t3.isConnected(t1));
    }
}
