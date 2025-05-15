package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChangeTerritoryMessageUnitTest {
    private ArrayList<Territory> territoryList;
    ChangeTerritoryMessage msg;

    @BeforeEach
    void setUp() {
        Territory t1 = new Territory(UUID.randomUUID(), 11, 1);
        Territory t2 = new Territory(UUID.randomUUID(), 11, 2);
        territoryList = new ArrayList<>(List.of(t1, t2));
        msg = new ChangeTerritoryMessage(territoryList);
    }

    @Test
    void testConstructorAndGetters() {
        ChangeTerritoryMessage msg = new ChangeTerritoryMessage(territoryList);
        assertEquals(territoryList, msg.territories());
        assertEquals(MessageType.UPDATE_TERRITORIES, msg.getType());
    }

    @Test
    void testEmptyTerritoryList() {
        territoryList.clear();

        assertNotNull(msg.territories());
        assertTrue(msg.territories().isEmpty());
        assertEquals(MessageType.UPDATE_TERRITORIES, msg.getType());
    }
}
