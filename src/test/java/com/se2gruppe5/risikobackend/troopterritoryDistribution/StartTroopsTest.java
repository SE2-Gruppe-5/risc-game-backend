package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class StartTroopsTest {
    @Test
    void testDistributeStartingTroops_basicDistribution() {
        StartTroops distributor = new StartTroops();
        List<String> territories = List.of("A", "B", "C");
        int totalTroops = 6;

        Map<String, Map<String, Integer>> result = distributor.distributeStartingTroops(territories, totalTroops);
        Map<String, Integer> troopMap = result.get("troops");

        assertEquals(3, troopMap.size());
        assertEquals(6, troopMap.values().stream().mapToInt(Integer::intValue).sum());
        assertTrue(troopMap.values().stream().allMatch(t -> t >= 1));
    }

    @Test
    void testDistributeStartingTroops_notEnoughTroops_throws() {
        StartTroops distributor = new StartTroops();
        List<String> territories = List.of("A", "B", "C");

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
