package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class AssignTerritories {
    private final Random random = new SecureRandom();
    public Map<UUID, List<Integer>> assignTerritories(List<UUID> players, List<Integer> territoryIds) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("No players");
        }
        if (territoryIds.size() % players.size() != 0) {
            throw new IllegalArgumentException("Territories must divide evenly among players");
        }
        Collections.shuffle(territoryIds, random);
        Map<UUID, List<Integer>> result = new ConcurrentHashMap<>();
        int size = territoryIds.size() / players.size();

        for (int i = 0; i < players.size(); i++) {
            UUID playerId = players.get(i);
            int from = i * size;
            int to = from + size;
            result.put(playerId, new ArrayList<>(territoryIds.subList(from, to)));
        }

        return result;
    }
}
