package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AssignTerritoriesTest {

    @Test
    void testAssignTerritories_evenDistribution() {
        AssignTerritories assigner = new AssignTerritories();
        List<String> players = List.of("P1", "P2");
        List<String> territories = List.of("A", "B", "C", "D");

        Map<String, List<String>> result = assigner.assignTerritories(players, territories);
        assertEquals(2, result.size());
        assertTrue(result.values().stream().allMatch(list -> list.size() == 2));
    }

    @Test
    void testAssignTerritories_invalidDivision_throws() {
        AssignTerritories assigner = new AssignTerritories();
        List<String> players = List.of("P1", "P2");
        List<String> territories = List.of("A", "B", "C");

        assertThrows(IllegalArgumentException.class, () ->
                assigner.assignTerritories(players, territories));
    }

    @Test
    void testAssignTerritories_noPlayers_throws() {
        AssignTerritories assigner = new AssignTerritories();

        assertThrows(IllegalArgumentException.class, () ->
                assigner.assignTerritories(List.of(), List.of("A", "B")));
    }

}
