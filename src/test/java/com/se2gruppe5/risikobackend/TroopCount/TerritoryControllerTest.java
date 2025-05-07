package com.se2gruppe5.risikobackend.TroopCount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TerritoryControllerTest {

    private TerritoryController controller;

    @BeforeEach
    void setUp() {
        controller = new TerritoryController(new TerritoryManager());
    }

    @Test
    void testGetTerritoriesForPlayer() {
        List<TerritoryRecord> result = controller.getTerritoriesForPlayer("player1");
        assertEquals(2, result.size());
    }

    @Test
    void testGetTerritoryByIdFound() {
        ResponseEntity<TerritoryRecord> response = controller.getTerritoryById(1);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetTerritoryByIdNotFound() {
        ResponseEntity<TerritoryRecord> response = controller.getTerritoryById(99);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateTroopsSuccess() {
        TerritoryRecord update = new TerritoryRecord(1, "player1", 33);
        ResponseEntity<Void> response = controller.updateTroops(update);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateTroopsNotFound() {
        TerritoryRecord update = new TerritoryRecord(99, "player1", 33);
        ResponseEntity<Void> response = controller.updateTroops(update);
        assertEquals(404, response.getStatusCodeValue());
    }
}
