package com.se2gruppe5.risikobackend.TroopCount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TerritoryRecordTest {
    @Test
    void testConstructorAndGettersSetters() {
        TerritoryRecord record = new TerritoryRecord(1, "playerX", 10);

        assertEquals(1, record.getId());
        assertEquals("playerX", record.getOwner());
        assertEquals(10, record.getTroops());

        record.setId(2);
        record.setOwner("playerY");
        record.setTroops(15);

        assertEquals(2, record.getId());
        assertEquals("playerY", record.getOwner());
        assertEquals(15, record.getTroops());
    }

}
