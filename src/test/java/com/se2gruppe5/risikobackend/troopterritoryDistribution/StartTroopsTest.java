package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class StartTroopsTest {
    @Test
    void testDistributeStartingTroops_basicDistribution() {
        StartTroops distributor = new StartTroops();
        List<Integer> territories = List.of(1, 2, 3);
        int totalTroops = 6;

        Map<Integer, Integer> result = distributor.distributeStartingTroops(territories, totalTroops);

        assertEquals(3, result.size());
        assertEquals(6, result.values().stream().mapToInt(Integer::intValue).sum());
        assertTrue(result.values().stream().allMatch(t -> t >= 1));
    }

    @Test
    void testDistributeStartingTroops_notEnoughTroops_throws() {
        StartTroops distributor = new StartTroops();
        List<Integer> territories = List.of(1, 2, 3);

        assertThrows(IllegalArgumentException.class, () ->
                distributor.distributeStartingTroops(territories, 2));
    }

    @Test
    void testDistributeStartingTroops_emptyTerritories_throws() {
        StartTroops distributor = new StartTroops();

        assertThrows(IllegalArgumentException.class, () ->
                distributor.distributeStartingTroops(List.of(), 10));
    }
}
