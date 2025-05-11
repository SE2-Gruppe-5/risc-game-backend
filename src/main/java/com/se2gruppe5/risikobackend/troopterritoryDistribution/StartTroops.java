package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class StartTroops {
    private final Random random = new SecureRandom();
    public Map<Integer, Integer> distribute(List<Integer> territoryIds, int totalTroops) {
        if (territoryIds.isEmpty()) throw new IllegalArgumentException("No territories");
        if (totalTroops < territoryIds.size()) throw new IllegalArgumentException("Too few troops");

        Map<Integer, Integer> result = new ConcurrentHashMap<>();
        territoryIds.forEach(id -> result.put(id, 1));
        int remaining = totalTroops - territoryIds.size();

        while (remaining > 0) {
            int randomId = territoryIds.get(random.nextInt(territoryIds.size()));
            result.put(randomId, result.get(randomId) + 1);
            remaining--;
        }

        return result;
    }
}
