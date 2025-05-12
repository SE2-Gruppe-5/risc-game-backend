package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AssignTerritoriesTest {

    @Test
    void testAssignTerritories_evenDistribution() {
        AssignTerritories assigner = new AssignTerritories();
        List<UUID> players = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<Integer> territories = new ArrayList<>(List.of(1, 2, 3, 4));

        Map<UUID, List<Integer>> result = assigner.assignTerritories(players, territories);
        assertEquals(2, result.size());
        assertTrue(result.values().stream().allMatch(list -> list.size() == 2));
    }

    @Test
    void testAssignTerritories_invalidDivision_throws() {
        AssignTerritories assigner = new AssignTerritories();
        List<UUID> players = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<Integer> territories = List.of(1, 2, 3);

        assertThrows(IllegalArgumentException.class, () ->
                assigner.assignTerritories(players, territories));
    }

    @Test
    void testAssignTerritories_noPlayers_throws() {
        AssignTerritories assigner = new AssignTerritories();

        assertThrows(IllegalArgumentException.class, () ->
                assigner.assignTerritories(List.of(), List.of(1, 2)));
    }

}
